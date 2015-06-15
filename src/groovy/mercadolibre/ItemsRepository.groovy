package mercadolibre

import itemsconsumer2.Items

/**
 * Created by dabdala on 2/6/15.
 */
class ItemsRepository {


    public def getItems(String idItem){

        def item = itemsconsumer2.Items.findByIditem(idItem)

        def itemMapa = [id: item.id, iditem: item.iditem, site_id: item.site_id,title: item.title,permalink: item.permalink,acepta_mp: item.acepta_mp]

        return itemMapa
    }

    public void saveItems(def itemMapa) {
        def item = new itemsconsumer2.Items(iditem: itemMapa.iditem, site_id: itemMapa.site_id, title: itemMapa.title, permalink: itemMapa.permalink, acepta_mp: itemMapa.acepta_mp)

        item.save(failOnError: true)

        itemMapa.non_mercado_pago_payment_methods.each{
            saveNonMPPaymentMethods(it,itemMapa.iditem)
        }
    }

    public void saveNonMPPaymentMethods(def nonMPMapa,def iditem) {
        def nonMPPayMet = new itemsconsumer2.NonMPPaymentMethods(idnonmp: nonMPMapa.id, iditem: iditem, description: nonMPMapa.description, type: nonMPMapa.type)
        nonMPPayMet.save(failOnError: true)
    }


}