lines = load 'seance2/licences_2012.csv' using PigStorage(';') as (cog2: chararray,libelle: chararray,fed_2012: int,l_2012: int,l_0_4_2012: int,l_5_9_2012: int,l_10_14_2012: int,l_15_19_2012: int,l_20_29_2012: int,l_30_44_2012: int,l_45_59_2012: int,l_60_74_2012: int,l_75_99_2012: int,l_f_2012: int,l_0_4_f_2012: int,l_5_9_f_2012: int,l_10_14_f_2012: int,l_15_19_f_2012: int,l_20_29_f_2012: int,l_30_44_f_2012: int,l_45_59_f_2012: int,l_60_74_f_2012: int,l_75_99_f_2012: int,l_h_2012: int,l_0_4_h_2012: int,l_5_9_h_2012: int,l_10_14_h_2012: int,l_15_19_h_2012: int,l_20_29_h_2012: int,l_30_44_h_2012: int,l_45_59_h_2012: int,l_60_74_h_2012: int,l_75_99_h_2012: int,l_zus_2012: int,l_zus_f_2012: int,l_zus_h_2012: int,pop_2010: int,pop_0_4_2010: int,pop_5_9_2010: int,pop_10_14_2010: int,pop_15_19_2010: int,pop_20_29_2010: int,pop_30_44_2010: int,pop_45_59_2010: int,pop_60_74_2010: int,pop_75_99_2010: int,popf_2010: int,popf_0_4_2010: int,popf_5_9_2010: int,popf_10_14_2010: int,popf_15_19_2010: int,popf_20_29_2010: int,popf_30_44_2010: int,popf_45_59_2010: int,popf_60_74_2010: int,popf_75_99_2010: int,poph_2010: int,poph_0_4_2010: int,poph_5_9_2010: int,poph_10_14_2010: int,poph_15_19_2010: int,poph_20_29_2019: int,poph_30_44_2010: int,poph_45_59_2010: int,poph_60_74_2010: int,poph_75_99_2010: int);
line_for_lille = filter lines by libelle=='LILLE';
desc_lille_by_licences = order lines_for_lille by l_2012 desc;
desc_lille_federation = foreach desc_lille_by_licences generate fed_2012, l_2012;
store desc_lille_federation into 'seance2/java_desc_licences_per_fede';
