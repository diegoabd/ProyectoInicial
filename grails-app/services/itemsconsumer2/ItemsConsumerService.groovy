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
    static transactional = false

//servicio principal
    public void executeProcesMessaje(def result)
    {
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
            if (respuesta!=null){
                saveError(mapaItems)
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveError(def mapaItems) {
        def savedException
        //manejar transaccion para realizar la insercion de datos en las tablas Items y nonMPPaymentsMethods
        try {
            Logs.withTransaction { tx ->
                try{
                    def error = [iditem: mapaItems.iditem, description: "ERROR DE ORACLE", fecha: "01/01/2010"]
                    itemsRepository.saveError(error)
                }catch(Throwable e){
                    log.info "Processed error"
                    savedException = e
                    tx.setRollbackOnly()
                }
            }
        } finally {
            if(savedException){
                println("Error guardado")
            }else{
                log.info "Processed correctly"
            }
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public def saveBase(def mapaItems){
        def savedException
        //manejar transaccion para realizar la insercion de datos en las tablas Items y nonMPPaymentsMethods
        try {
            Items.withTransaction { tx ->
                try{
                    //guardar datos en las tablas correspondientes (se maneja con un solo metodo porque tabla hija debe ser transaccionada con la tabla padre
                    itemsRepository.saveItems(mapaItems)
                }catch(Throwable e){
                    log.info "Processed error"
                    savedException = e
                    tx.setRollbackOnly()
                }
            }
        } finally {
            if(savedException){
                println("Error en carga de datos: " + mapaItems.iditem)
                return savedException
             }else{
                println("Proceso Bien. IdItem: " + mapaItems.iditem)
                log.info "Processed correctly"
                return savedException
                }
            }
    }

    public def getBase(String iditem){
        //validacion segun el resultado del guardado en la base
        def Items item
        try {
            item = itemsRepository.getItems(iditem)
        }catch (Throwable e){
            return null
        }finally {
            return item
        }
    }

}
