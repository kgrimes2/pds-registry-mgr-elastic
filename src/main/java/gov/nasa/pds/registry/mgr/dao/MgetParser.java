package gov.nasa.pds.registry.mgr.dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;


public class MgetParser
{
    public static class Record
    {
        public String id;
        public boolean found;
        public String esDataType;
    }
    
    
    private JsonReader rd;

    
    public MgetParser()
    {
    }
    
    
    public List<Record> parse(HttpEntity entity) throws IOException
    {
        List<Record> records = new ArrayList<>();
        
        InputStream is = entity.getContent();
        rd = new JsonReader(new InputStreamReader(is));
    
        rd.beginObject();
        
        while(rd.hasNext() && rd.peek() != JsonToken.END_OBJECT)
        {
            String name = rd.nextName();
            if("docs".equals(name))
            {
                rd.beginArray();
                while(rd.hasNext() && rd.peek() != JsonToken.END_ARRAY)
                {
                    Record rec = parseDoc();
                    records.add(rec);
                }
                rd.endArray();
            }
            else
            {
                rd.skipValue();
            }
        }
        
        rd.endObject();
        rd.close();
        
        return records;
    }
    
    
    private Record parseDoc() throws IOException
    {
        Record rec = new Record();
        
        rd.beginObject();
        
        while(rd.hasNext() && rd.peek() != JsonToken.END_OBJECT)
        {
            String name = rd.nextName();
            if("_id".equals(name))
            {
                rec.id = rd.nextString();
            }
            else if("found".equals(name))
            {
                rec.found = rd.nextBoolean();
            }
            else if("_source".equals(name))
            {
                parseSource(rec);
            }
            else
            {
                rd.skipValue();
            }
        }
        
        rd.endObject();
        
        return rec;
    }
    
    
    protected void parseSource(Record rec) throws IOException
    {
        rd.beginObject();
        
        while(rd.hasNext() && rd.peek() != JsonToken.END_OBJECT)
        {
            String name = rd.nextName();
            if("es_data_type".equals(name))
            {
                rec.esDataType = rd.nextString();
            }
            else
            {
                rd.skipValue();
            }
        }
        
        rd.endObject();
    }

}
