

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;


public class DataBean {

	private static Date cur_min_date;
	protected static TreeMap<String, String> list = new TreeMap<>();
	protected static List<Vertex> list_of_vertices = new ArrayList<>();
	protected static int skippedTags;
	
	String date;
	String line;

	public DataBean(String date_string, String line) 
	{
		Date date =getDateFromString(date_string);
		if (cur_min_date == null) 
		{
			cur_min_date = date;
		} 
		else 
		{
			if (cur_min_date.compareTo(date) > 0)
				cur_min_date = date;
		}
		this.date = date_string;
		this.line = line;
	}

	static void calculateAverage(FileWriter wr) throws IOException 
	{
		int prev_count=0;
		for (Map.Entry<String,String> entry:list.entrySet()) 
		{
			int size_that_instant=Integer.parseInt(entry.getKey().split(";")[1]);
			String tags[]=entry.getValue().split(",");
			int neighbour_count=0;
			for(String tag:tags)
			{
				Vertex v=new Vertex(tag);
				int index=list_of_vertices.indexOf(v);
				if(index!=-1)
					v=list_of_vertices.get(index);
				neighbour_count+=v.getNeighborCount();
			}
			
			prev_count+=neighbour_count;
			wr.write(String.format("%.2f", (float) (prev_count) / (float) size_that_instant));
			wr.write("\n");
		}
	}

	//Creating or updating the hashtag graph for each Tweet input
	void generateGraphOverCSV() 
	{
		String date = this.date;
		String line = this.line;
		String tags[] = line.split("\\,");
		
		//Maintaining Data within the 60 second window
		modifyListBasedOnDate(getDateFromString(date));
		
		for (int j = 0; j < tags.length; j++) 
		{
			Vertex v1 = new Vertex(tags[j]);
			if (list_of_vertices.contains(v1)) 
			{
				int index = list_of_vertices.indexOf(v1);
				v1 = list_of_vertices.remove(index);
			}
			for (int i = 0; i < tags.length; i++) 
			{
				Vertex v2 = new Vertex(tags[i]);
				if (!v2.getLabel().equals(v1.getLabel())) 
				{
					Edge e = new Edge(v1, v2);
					if (!v1.containsNeighbor(e)) 
					{
						v1.addNeighbor(e);
					}
				}
			}
			if(tags.length > 1)
				list_of_vertices.add(v1);
		}
		String toInsert=date+";"+list_of_vertices.size()+";"+System.currentTimeMillis();
		list.put(toInsert, line);
		if (cur_min_date == null)
			cur_min_date = getDateFromString(list.firstKey().split(";")[0]);
	}

	protected static Date getDateFromString(String date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);
		try 
		{
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//Maintaining Data within the 60 second window
	protected static void modifyListBasedOnDate(Date date) 
	{
		int sum = calculateCalendarSum(cur_min_date);
		int s_sum = calculateCalendarSum(date);

		if (s_sum > (sum + 60)) 
		{
			String key = list.firstKey();
			if (key != null) 
			{
				String tags[] = list.get(key).split(",");
				for (String tag : tags) 
				{
					Vertex v = new Vertex(tag);
					if (list_of_vertices.contains(v)) 
					{
						int index = list_of_vertices.indexOf(v);
						list_of_vertices.remove(index);
					}
				}
				list.remove(key);
				cur_min_date = null;
			}
		}
	}

	private static int calculateCalendarSum(Date date) 
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int hours = c.get(Calendar.HOUR_OF_DAY);
		int minutes = Calendar.MINUTE;
		int seconds = c.get(Calendar.SECOND);
		int sum = hours * 10000 + minutes * 100 + seconds;
		return sum;
	}
}
