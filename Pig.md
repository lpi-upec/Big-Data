# Apache Pig

## Introduction

Pig est un framework JAVA, il fournit un language Pig Latin et un language Pig Grunt.

Un utilisateur écrit au Pig Latin un script, nommé Licences.pig.  
Un utilisateur fournit au Pig Grunt le fichier Licences.pig par interpolation.  
Pig Offre 2 modes de fonctionnement :

* par fichier local
* par cluster Hadoop

Par rapport a Hadoop, Pig utilise MAP/REDUCE 2.0

Pig met en place 3 niveaux de structures de données :

* Field : une cellule d'une table avec une donnée (Cellule d'une ligne)
* Tuple : un produit cartésien de Fields (Ligne D'une table)
* Bag : un sac de Tuples (Table)

Les types de données : `int, float, double, float, bytearray, chararray, short`

Pig est un ETL (Extract-transform-load) :

* Load : pour charger un fichier depuis une source

`load <source> using <function> as <schema>`

* Store : pour sauvegarder un calcul (HDFS, Hive, HBase)

`store records into <destination> using <function>`

Entre `load` et `store`, il faut réaliser un traitement :

* Filter : pour filtrer les données
* Group : pour regrouper les tuples dans le but de faire un calcul
* Order : pour trier des tuples par rapport à une ou plusieurs clés
* Foreach : pour boucler sur un `bag`
* Flatten : pour parcourir un bag en profondeur et ôter un niveau de structuration
* Split : pour découper un tuples en `Fields`
* Limit : pour borner un bag
* Distinct : pour enlever les doublons d'un `bag`
* Cross : pour mélanger des tuples venant de 2 `bag` différents
* Sample : pour générer de tuples à partir d'un schéma

## Installation

* Extraire Pig

```sh
sudo tar xzf pig.tar.gz -C /opt
sudo chown -R hduser:hadoop /opt/pig
```

* Modifier le .bashrc

```sh
export PIG_HOME=/opt/pig
export PATH=$PIG_HOME/bin:$PATH
```

* Recharger .bashrc

```sh
source ~/.bashrc
```

* Démarrer l'outil jobhistory de HADOOP

```sh
mr-jobhistory-daemon.sh start historyserver
```

* Démarrer pig en local ou avec hadoop

local : `pig -x local`
hadoop : `pig`

## Utilisation

Dans Pig :

* Charger le fichier depuis Hadoop

```
line = load 'seance2/licences_2012.csv' using PigStorage(';');

lille = filter line by $1 == 'LILLE';

dump lille
```


Exemple TP6 Pig

```
lines = load ‘machin/truc’ using PigStorage(‘;’) as (cog2: chararray,libelle: chararray,fed_2012: int,l_2012: int,l_0_4_2012: int,l_5_9_2012: int,l_10_14_2012: int,l_15_19_2012: int,l_20_29_2012: int,l_30_44_2012: int,l_45_59_2012: int,l_60_74_2012: int,l_75_99_2012: int,l_f_2012: int,l_0_4_f_2012: int,l_5_9_f_2012: int,l_10_14_f_2012: int,l_15_19_f_2012: int,l_20_29_f_2012: int,l_30_44_f_2012: int,l_45_59_f_2012: int,l_60_74_f_2012: int,l_75_99_f_2012: int,l_h_2012: int,l_0_4_h_2012: int,l_5_9_h_2012: int,l_10_14_h_2012: int,l_15_19_h_2012: int,l_20_29_h_2012: int,l_30_44_h_2012: int,l_45_59_h_2012: int,l_60_74_h_2012: int,l_75_99_h_2012: int,l_zus_2012: int,l_zus_f_2012: int,l_zus_h_2012: int,pop_2010: int,pop_0_4_2010: int,pop_5_9_2010: int,pop_10_14_2010: int,pop_15_19_2010: int,pop_20_29_2010: int,pop_30_44_2010: int,pop_45_59_2010: int,pop_60_74_2010: int,pop_75_99_2010: int,popf_2010: int,popf_0_4_2010: int,popf_5_9_2010: int,popf_10_14_2010: int,popf_15_19_2010: int,popf_20_29_2010: int,popf_30_44_2010: int,popf_45_59_2010: int,popf_60_74_2010: int,popf_75_99_2010: int,poph_2010: int,poph_0_4_2010: int,poph_5_9_2010: int,poph_10_14_2010: int,poph_15_19_2010: int,poph_20_29_2019: int,poph_30_44_2010: int,poph_45_59_2010: int,poph_60_74_2010: int,poph_75_99_2010: int);
```
