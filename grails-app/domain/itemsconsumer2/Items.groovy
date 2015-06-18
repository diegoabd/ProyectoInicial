package itemsconsumer2
/**
 * Created by dabdala on 2/6/15.
 */
class Items {
    String  iditem
    String  site_id
    String  title
    String  permalink
    String  acepta_mp

    static constraints = {
    }

    static mapping = {
        version false
        id generator: "increment"
    }

    @Override
      public String toString() {
        return "Items [iditem:" + iditem + "][site_id:" +
                site_id + "][title:" + title + "][permalink:" + permalink + "][acepta_mp:" + acepta_mp + "]";
    }


}
//new item(non_mp_payment_methods:new itemsconsumer2.NonMPPaymentMethods()).save()

/*def a = itemsconsumer2.Items.get(1)
for (non_mp_payment_methods in a.non_mp_payment_methods) {
    println non_mp_payment_methods.description
}*/