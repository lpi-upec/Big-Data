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
