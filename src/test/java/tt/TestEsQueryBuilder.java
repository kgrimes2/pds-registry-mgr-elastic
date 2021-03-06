package tt;

import gov.nasa.pds.registry.mgr.util.es.EsRequestBuilder;

public class TestEsQueryBuilder
{
    public static void main(String[] args) throws Exception
    {
        testMatchAll();
        System.out.println();
        
        testDelete();
        System.out.println();
        
        testUpdateStatus();
        System.out.println();
    }


    private static void testMatchAll() throws Exception
    {
        EsRequestBuilder bld = new EsRequestBuilder(true);
        String json = bld.createMatchAllQuery();
        System.out.println(json);
    }

    
    private static void testDelete() throws Exception
    {
        EsRequestBuilder bld = new EsRequestBuilder(true);
        String json = bld.createFilterQuery("lidvid", "test::1.0");
        System.out.println(json);
    }

    
    private static void testUpdateStatus() throws Exception
    {
        EsRequestBuilder bld = new EsRequestBuilder(true);
        String json = bld.createUpdateStatusRequest("STAGED", "lidvid", "test::1.0");
        System.out.println(json);
    }
}
