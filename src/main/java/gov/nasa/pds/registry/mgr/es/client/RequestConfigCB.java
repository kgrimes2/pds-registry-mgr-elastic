package gov.nasa.pds.registry.mgr.es.client;

import org.apache.http.client.config.RequestConfig;
import org.elasticsearch.client.RestClientBuilder;

public class RequestConfigCB implements RestClientBuilder.RequestConfigCallback
{
    private int connectTimeoutSec = 5;
    private int socketTimeoutSec = 10;
    
    
    public RequestConfigCB()
    {
    }

    
    public RequestConfigCB(int connectTimeoutSec, int socketTimeoutSec)
    {
        this.connectTimeoutSec = connectTimeoutSec;
        this.socketTimeoutSec = socketTimeoutSec;
    }

    
    @Override
    public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder bld)
    {
        bld.setConnectTimeout(connectTimeoutSec * 1000);
        bld.setSocketTimeout(socketTimeoutSec * 1000);
        return bld;
    }

}
