lines = load '/home/edenskull/Téléchargements/BX-Books.csv' using PigStorage(';') as (isbn:chararray, bt:chararray, ba:chararray, yop:chararray, pub:chararray, ius:chararray, ium:chararray, iul:chararray);

only_year = foreach lines generate yop;

substring_year = foreach only_year generate SUBSTRING(yop,1,5) as (yop);

year_parsed = foreach substring_year generate (int)yop as yop;

group_by_year = group year_parsed by yop;

count_by_year = foreach group_by_year generate group, COUNT(year_parsed);

store count_by_year into '/home/hduser/count_by_year';
