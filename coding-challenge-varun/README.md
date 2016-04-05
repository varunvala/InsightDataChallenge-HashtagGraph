Insight Data Engineering - Coding Challenge
===========================================================

The CODE/LOGIC_FLOW :::
1)parseData() method consumes each tweet line as the input and parses out <created_at> & <hashtags> to form a <average_degree> Object
2)<average_degree> Object has all the info. needed for the tweet to do our hashtag graph processing
3)modifyListBasedOnDate() method maintains data in the last 60seconds window
-->The above 3 steps are followed for each tweet line input.
4)Then calculateAverage() method calculates the UPDATED average degree for the vertices of the graph after each of the tweet inputs that we processed before.


DEPENDENCIES :::
1)Json parsing jar, commons-io jar, commons-lang3 jar are used for the project. These are included in the /bin folder for REFERENCE
2)A final RUNNABLE JAR is created and is USED in the run.sh script to trigger the project.

HOW To RUN/CHECK the results?
->Nothing is changed. Just run the <run_tests.sh> from the insight_testsuite directory.




