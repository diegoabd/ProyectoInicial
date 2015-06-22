package itemsconsumer2

import org.springframework.transaction.annotation.Propagation
import grails.transaction.Transactional
import mercadolibre.*

class ItemsConsumerService{
    def redisMananger
    def whereToConnectJedis ="localhost"
    def idListJedisToConnect="ITEMS_LIST"
    def delimitador = ","
    def itemsRepository
    def itemsClient
//    static transactional = false

//servicio principal
    public def executeProcesMessaje(def result){
        def items

        //obtener items del redis, ya separados por coma en una lista.
        if (result == "") {
            items = splitData(redisMananger.getRedistItems(idListJedisToConnect, whereToConnectJedis), delimitador)
        }else{
            items = splitData(result,delimitador)
        }

        //recorrer cada dato que este guardado en el redis
        items.each{
            //ejecutar curl
            //manipular json
            def mapaItems = getJson(it)
            //guardar en tablas Items y nonMpPaymentMethods
            def respuesta = saveBase(mapaItems)
            return respuesta
        }
    }

    public List<String> splitData(String jedis, String delimitador){
        def listaItems
        //separar los datos por el delimitador
        listaItems = jedis.split(delimitador)

        return listaItems
    }

    public def getJson(String idItem){
        //obtener los datos de la api
        def json = itemsClient.getItem(idItem)
        //pasarlos a un mapa
        def mapaItems = [iditem: json.id,site_id: json.site_id, title: json.title, permalink: json.permalink, acepta_mp: json.accepts_mercadopago,non_mercado_pago_payment_methods: json.non_mercado_pago_payment_methods]

        return mapaItems
    }


    public def saveBase(def mapaItems){
        //guardar datos en las tablas correspondientes (se maneja con un solo metodo porque tabla hija debe ser transaccionada con la tabla padre
        def ItemSaved = itemsRepository.saveItems(mapaItems)

        return ItemSaved
    }

    public def getBase(String iditem){
        //validacion segun el resultado del guardado en la base
        def Items item
        item = itemsRepository.getItems(iditem)

        return item
    }

}
