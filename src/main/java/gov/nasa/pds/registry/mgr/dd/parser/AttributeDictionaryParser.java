package gov.nasa.pds.registry.mgr.dd.parser;

import java.io.File;
import com.google.gson.stream.JsonToken;
import gov.nasa.pds.registry.mgr.dd.parser.DDAttribute;


public class AttributeDictionaryParser extends BaseDDParser
{
    public static interface Callback
    {
        public void onAttribute(DDAttribute attr) throws Exception;
    }
    
    //////////////////////////////////////////////////////////////////////
    
    private Callback cb;
    private int itemCount;

    
    public AttributeDictionaryParser(File file, Callback cb) throws Exception
    {
        super(file);
        this.cb = cb;
    }
    
    
    @Override
    protected void parseAttributeDictionary() throws Exception
    {
        System.out.println("Parsing attribute dictionary...");
        
        jsonReader.beginArray();
        
        while(jsonReader.hasNext() && jsonReader.peek() != JsonToken.END_ARRAY)
        {
            jsonReader.beginObject();

            while(jsonReader.hasNext() && jsonReader.peek() != JsonToken.END_OBJECT)
            {
                String name = jsonReader.nextName();
                if("attribute".equals(name))
                {
                    parseAttr();
                }
                else
                {
                    jsonReader.skipValue();
                }
            }
            
            jsonReader.endObject();
        }
        
        jsonReader.endArray();
    }


    private void parseAttr() throws Exception
    {
        itemCount++;
        
        DDAttribute attr = new DDAttribute();
        
        jsonReader.beginObject();
        
        while(jsonReader.hasNext() && jsonReader.peek() != JsonToken.END_OBJECT)
        {
            String name = jsonReader.nextName();
            if("identifier".equals(name))
            {
                attr.id = jsonReader.nextString();
                String tokens[] = attr.id.split("\\.");
                if(tokens.length != 5) throw new Exception("Could not parse attribute id " + attr.id);
                
                attr.classNs = tokens[1];
                attr.className = tokens[2];
                attr.attrNs = tokens[3];
                attr.attrName = tokens[4];
            }
            else if("dataType".equals(name))
            {
                attr.dataType = jsonReader.nextString();
            }
            else if("description".equals(name))
            {
                attr.description = jsonReader.nextString();
            }
            else
            {
                jsonReader.skipValue();
            }
        }
        
        jsonReader.endObject();
        
        if(attr.id == null)
        {
            String msg = "Missing identifier in attribute definition. Index = " + itemCount;
            throw new Exception(msg);
        }
        
        if(attr.dataType == null) 
        {
            String msg = "Missing dataType in attribute definition " + attr.id;
            throw new Exception(msg); 
        }

        cb.onAttribute(attr);
    }

}
