package mercadolibre

/**
 * Created by dabdala on 29/5/15.
 */
class RESTItemClient extends InternalRESTClient {

    def getItem(id) {
        return getResource("/items/${id}")
    }
}
