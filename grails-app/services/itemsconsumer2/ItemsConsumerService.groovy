package itemsconsumer2

import org.springframework.transaction.annotation.Propagation
import redis.clients.jedis.Jedis
import grails.transaction.Transactional
import mercadolibre.*

class ItemsConsumerService{

    Jedis jedis
    def whereToConnectJedis ="localhost"
    def idListJedisToConnect="ITEMS_LIST"
    def delimitador = ","
    def itemsRepository
    def itemsClient
    static transactional = false



    public void executeProcesMessaje(def result)
    {
        def items
        def listaItems
        def Json

        //obtener items del redis, ya separados por coma en una lista.
        items = splitDatos(getRedistItems(),delimitador)

        items.each{

            //ejecutar curl
            //manipular json

            def mapaItems = obtenerJson(it)
            guardarEnBase(mapaItems)

           // def site_id = consultarBase(mapaItems.iditem)

        }

    }

    public String getRedistItems() {
        initializeJedis()

        String value = jedis.get(idListJedisToConnect)
        return value
    }

    def initializeJedis(){
        jedis = new Jedis(whereToConnectJedis)
        jedis.connect()
    }

    public List<String> splitDatos(String jedis, String delimitador){
        def listaItems

        listaItems = jedis.split(delimitador)

        return listaItems
    }

    public def obtenerJson(String idItem){
        def json = itemsClient.getItem(idItem)
        def mapaItems = [iditem: json.id,site_id: json.site_id, title: json.title, permalink: json.permalink, acepta_mp: json.accepts_mercadopago,non_mercado_pago_payment_methods: json.non_mercado_pago_payment_methods ]

        return mapaItems
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void guardarEnBase(def mapaItems){
        def savedException

        try {
            Items.withTransaction { tx ->
                try{
                    itemsRepository.saveItems(mapaItems)
                }catch(Throwable e){
                    println("posible error")
                    log.info "processed error"
                    savedException = e
                    tx.setRollbackOnly()
                }
            }
        } finally {
            if(savedException){
                println("error")
             }else{
                println("Proceso Bien")
                log.info "processed correctly"
                }
            }
    }

    public def consultarBase(String iditem){
        //validacion segun el resultado del guardado en la base
        Items item = itemsRepository.getItems(iditem)
        //println("item en base: $item")

        return item.site_id
    }

}
