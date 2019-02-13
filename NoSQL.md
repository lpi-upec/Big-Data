# Base NoSQL

## Introduction

On souhaite paralléliser des bases sur un cluster pour distribuer une table.  
Il existe 4 formes de bases NoSQL :
* Orientée colonne (cassandra, hbase)
* Orientée document (mongoDB)
* Clé/Valeur "cache mémoire" (apache accumulo, redis)
* Orientée graphe (Neo4j)

Dans une base relationnelle PostgreSQL en cluster :
* Cohérence : Quelque soit le noeud du cluster, une requête donne le même résultat
* Disponibilité : la perte d'un noeud n'entraîne pas l'indisponibilité du SGBDR
* Résistance au morcellement : chaque noeud doit pouvoir fonctioner de la façon identiques aux autres (autonomie)

Choisir une base NoSQL signifie ne pas considérer une de ces propriétés.  
Si on souhaite une base orientée colonne :
* cassandra : architecture peer to peer
* Hbase : architecture client/server

Dans tous les cas la notion de transaction est perdue.

### 1.1 Base orientée colonne

Une table sera découpée en famille de colonne. Toute les clauses `WHERE` doivent être satisfaite dans une seule famille.  
Exemple : HBase, Cassandra, Firebase, Vertica

### 1.2 Base orientée document

Un document signifie un enregistrement, par exemple un paquet json. Ce type de base supporte les clés étrangères => il est possible de faire des jointures.

### 1.3 Base orientée clé/valeur

Type de base utilisateur, servant à une reprise sur un incident (entre une phase de Map et une phase de Reduce)

## Installer HBase

* Download Apache Hbase

* Complete the installation process

* Uncompress it : `tar -zxvf hbase-1.2.5-bin.tar.gz -C /opt/`

* Edit `hbase-env.sh` :
```sh
export JAVA_HOME=YOUR_JDK_PATH
```

* Edit `hbase-site.xml` using this command lines :
```XML
<configuration>
    //Here you have to set the path where you want HBase to store its files.
    <property>
        <name>hbase.rootdir</name>
        <value>hdfs://localhost:9000/hbase</value>
    </property>
    //Here you have to set the path where you want HBase to store its built in zookeeper  files.
    <property>
        <name>hbase.zookeeper.property.dataDir</name>
        <value>/home/hadoopworkshop/zookeeper</value>
    </property>
    <property>
        <name>hbase.cluster.distributed</name>
        <value>true</value>
    </property>
</configuration>
```

* Edit `bashrc` :
```sh
# - HBASE ENVIRONMENT VARIABLES START -#
export HBASE_HOME=/usr/local/hbase
export PATH=$PATH:$HBASE_HOME/bin
# - HBASE ENVIRONMENT VARIABLES END -#
```

Update env var with .bashrc : `source .bashrc`

* Start hbase processes
    * Start hadoop process first:
    ```sh
    start-hdfs.sh
    start-yarn.sh
    ```
    * Start hbase:
    ```sh
    start-hbase.sh
    ```

* Stop hbase processes

    * Stop hbase:
    ```sh
    stop-hbase.sh
    ```
    * Stop hadoop process:
    ```sh
        stop-dfs.sh
        stop-yarn.sh
    ```

## HBase

Les données sont stockées dans des tables. Les tables sont découpées en famille de colonne. Les familles de colonnes sont découpées en colonnes typées.  
Plusieurs moyens d'accès à HBase :
* Shell HBase avec un language de commande NoSQL
* API Java avec un driver dédié

L'architecture logicielle est de type Maître/esclave

PHOTO

Hmaster affecte les régions aux HRegionserver
Hmaster est placé sur le NameServer
Les HRegionservers sont placés sur les DataNode
Hregion gère les accès aux Regions

### 2.1 Shell HBase

Le shell se lance via la commande `hbase shell`

DDL (create, list, drop, enable, disable ...)

DML (put, get ...)

Hbase dispose d'une configuration qui peut être modifiée permettent de fournir à chaque données un timstamp. De plus chaque cellule d'une table peut conserver la trace de plusieurs valeurs.

Utilisation du Shell HBase :
```
create ‘people, ‘civility’, ‘contact’
describe ‘people’    
put ‘people’, ‘p1’, ‘civility:nom’, ‘Tata’
put ‘people’, ‘p1’, ‘civility:prenom’, ‘Toto’
put ‘people’, ‘p1’, ‘contact:tel’, ‘0615014581’
put ‘people’, ‘p1’, ‘contact:email’, ‘test@u-pec.fr’
```

### 2.2 API Java

La classe principale est la classe HTable. La classe put sert à faire une insertion. La classe get sert à faire les accès.
La classe result permet de lister le résultats d'un `GET` ou d'un scan.

Pour inserer une donnée :
```JAVA
Configuration config = HbaseConfiguration.create();
Htable personne = new Htable(conf, "personne");
Put insert = new Put(Bytes.toByts("personne"));
insert.add(Bytes.toBytes("civilité"), Bytes.toBytes("nom"), Bytes.toBytes("deaf"));
insert.add(Bytes.toBytes("civilité"),Bytes.toBytes("prenom"), Bytes.toBytes("john"));
insert.add(Bytes.toBytes("contact"),Bytes.toBytes("tel"), Bytes.toBytes("06XXXXXXX"));
```
