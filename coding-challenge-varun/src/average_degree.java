import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class average_degree extends DataBean{
	
	average_degree(String date,String line){
		super(date,line);
	}

	public static void main(String[] args) throws IOException, ParseException
	{
		String inputPath = args[0];
		String outputPath = args[1];
		
		//List<String> lines=FileUtils.readLines(new File("tweet_input\\tweets.txt"));
		List<String> lines=FileUtils.readLines(new File(inputPath));
		
		
		//FileWriter wr = new FileWriter("tweet_output\\output.txt");
		//Creating a file writer for writing the average values to the output file.
		FileWriter wr = new FileWriter(outputPath);
		wr.flush();
		
		//Processing each Tweet line and creating/updating the Hashtag Graph
		for(String line:lines)
		{
			//parseData method consumes each tweet line as the input and parses out <created_at> & <hashtags> to form a <average_degree> Object
			average_degree object=parseData(line);
			if(object!=null)
			{
				//<average_degree> Object has all the info. needed for the tweet to do our hashtag graph processing
				object.generateGraphOverCSV();
			}
		}
		
		//Finally calculating the average degree for the vertices of the graph
		calculateAverage(wr);
		
		wr.close();
	}
	
	//parseData method consumes each tweet line as the input and parses out <created_at> & <hashtags> to form a <average_degree> Object
	public static average_degree parseData(String record) throws ParseException 
	{
		Object oj = null;
		average_degree avg_dg=null;
		
		try {
			oj = new JSONParser().parse(record.toString());
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
		
		JSONObject ob = (JSONObject) oj;
		String date = (String) ob.get("created_at");
		JSONObject csv = (JSONObject) ob.get("entities");
		
		if (csv != null) 
		{
			JSONArray tags = (JSONArray) csv.get("hashtags");
			if (tags != null && !tags.isEmpty() ) 
			{
				@SuppressWarnings("unchecked")
				Iterator<JSONObject> pq = tags.iterator();
				if (date != null) 
				{
					List<String> build = new ArrayList<>();
					while (pq.hasNext()) 
					{
						build.add((String) pq.next().get("text"));
					}
					String CSV_line = StringUtils.join(build, ",");
					avg_dg=new average_degree(date, CSV_line);
				}
			}
			else
				skippedTags++;
		}
		return avg_dg;
	}
}
