package mercadolibre

import grails.transaction.Transactional
import itemsconsumer2.Items
import org.springframework.transaction.annotation.Propagation

/**
 * Created by dabdala on 2/6/15.
 */
class ItemsRepository {



    public def getItems(String idItem){
        def item = itemsconsumer2.Items.findByIditem(idItem)

        def itemMapa = [id: item.id, iditem: item.iditem, site_id: item.site_id,title: item.title,permalink: item.permalink,acepta_mp: item.acepta_mp]

        return itemMapa
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public def saveItems(def itemMapa) {
        def item = new itemsconsumer2.Items(iditem: itemMapa.iditem, site_id: itemMapa.site_id, title: itemMapa.title, permalink: itemMapa.permalink, acepta_mp: itemMapa.acepta_mp)
        def itemSaved
        def itemMpSaved
        def savedException
        def tienenonMp = false
        //manejar transaccion para realizar la insercion de datos en las tablas Items y nonMPPaymentsMethods
        try {
            Items.withTransaction { tx ->
                try{
                    //guardar datos en las tablas correspondientes (se maneja con un solo metodo porque tabla hija debe ser transaccionada con la tabla padre
                    itemSaved = item.save(failOnError: true)

                    itemMapa.non_mercado_pago_payment_methods.each{
                        tienenonMp = true
                        itemMpSaved = saveNonMPPaymentMethods(it,itemMapa.iditem)
                    }

                }catch(Throwable e){
                    savedException = e
                    tx.setRollbackOnly()
                }
            }
        } finally {

            if (savedException!=null){
                Date fecha = new Date()
                def error = [iditem: itemMapa.iditem, description: savedException, fecha: fecha]
                saveError(error)

                return null
            }else{
                return itemSaved
            }
        }
    }

    public void saveNonMPPaymentMethods(def nonMPMapa,def iditem) {
        def nonMPPayMet = new itemsconsumer2.NonMPPaymentMethods(idnonmp: nonMPMapa.id, iditem: iditem, description: nonMPMapa.description, type: nonMPMapa.type)

        nonMPPayMet.save(failOnError: true, flush: true)
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveError(def error){
        def log = new itemsconsumer2.Logs(iditem: error.iditem, description: (String) error.description, fecha: "01-02-2014")// (String) error.fecha)
            log.save(failOnError: true)

    }


}