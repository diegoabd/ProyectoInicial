package itemsconsumer2

import itemsconsumer2.Items

/**
 * Created by dabdala on 2/6/15.
 */
class NonMPPaymentMethods {
    String idnonmp
    String iditem
    String description
    String type
    //static belongsTo = [iditem:Items]


    static constraints = {
    }

    static mapping = {
        version false
        table 'NonMPPaymentMethods'
        id generator: "increment"
    }

    @Override
    public String toString() {
        return "NonMPPaymentMethods [idnon:" + idnonmp + "][description:" +
                description + "][type:" + type + "]";
    }
}
