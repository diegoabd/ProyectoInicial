package mercadolibre

import org.apache.log4j.Logger

/**
 * Created by dabdala on 29/5/15.
 */


abstract class InternalRESTClient {

    protected Logger log = Logger.getLogger(getClass())

    def restClient

    def encode(obj){
        URLEncoder.encode(obj.toString(),'UTF-8'.toString())
    }

    protected def getResource(url){

        def info

         restClient.get(uri: url.toString(),
                headers: null,
                success: { info = it.data },
                failure: {
                    if (it?.status == 404 || it?.status?.statusCode == 404 || it == null) {

                        return null
                    } else {

                        def errorMsg = "Error getting ${url} returned status: ${it?.status?.statusCode}"
                        if (it.exception){

                            log.error(errorMsg, it.exception)
                            log.error("Data log", it.data)
                            throw it.exception
                        } else {

                            log.error(errorMsg)
                            throw new RuntimeException("Error getting resource")

                        }
                    }
                },
        )

        return info
    }

}

