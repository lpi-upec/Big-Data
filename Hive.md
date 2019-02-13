# Hive Server

## Introduction

L'objectif de HiveServer sera :

* Lancer du code `JAVA` via l'API HBase
* Lancer un client pour accéder au données de la table a créer
* Faire des requêtes

Apache Hive va pouvoir utiliser une interprétation de MAP/REDUCE de SQL.  
Depuis 2010, Hive permet de faire des requêtes sur plusieurs support. Il joue l’intermédiaire entre SQL92 et un support de persistance NoSQL.

Plusieurs mode d'emploi pour Hive :

* Shell de commande (Hive CLI)
* API slave
* Interpréteur Hive depuis Thief

Il utilise une base HCatalogue. Elle contient les formats des supports réels sur lesquels les requêtes SQL doivent être traduites. Il permet le requêtage SQL sur un cluster de noeud Hadoop. L'interpréteur transforme les requêtes au MAP/REDUCE

PHOTO

## Installation

Pour fonctionner Hive a besoin de JAVA_HOME.

* Installer Hive

```
sudo tar xzf .../apache-hive -C /opt
sudo chown hduser:hadoop -R /opt/apache-hive
```

* Modifier le .bashrc

```
export HIVE_HOME=/opt/apache-hive
export PATH=$HIVE_HOME/bin:$PATH
```

* Dans HDFS, il faut lui préparer un répertoire :

```
hdfs dfs -mkdir /tmp
hdfs dfs -mkdir -p /user/hive/warehouse

hdfs dfs -chmod g+w /tmp
hdfs dfs -chmod g+w /user/hive/warehouse
```

* Verifier la configuration de YARN (yarn-site.xml)

```XML
<property>
    <name>mapred.job.tracker</name>
    <value>http://localhost:10040</value>
</property>
```

* Dupliquer le template hive-env.sh.template

```
sudo cp /opt/apache-hive/conf/hive-env.sh.template /opt/apache-hive/conf/hive-env.sh
```

* Decommenter la ligne HADOOP_HOME dans le fichier `hive-env.sh` et placer la bonne valeur

* Ajouter des propriétés dans `core-site.xml`

```XML
<configuration>
    <property>
        <name>hadoop.proxyuser.hduser.hosts</name>
        <value>*</value>
    </property>
    <property>
        <name>hadoop.proxyuser.hduser.groups</name>
        <value>*</value>
    </property>
</configuration>
```

* Ajouter une propriété dans `hive-site.xml`

```XML
<?xml version = 1.0 encoding =’UTF-8’?>
<configuration>
    <property>
        <name>hive.server2.authentification</name>
        <value>NONE</value>
    </property>
</configuration>

```

* Redémarrer HDFS et YARN

```
stop-yarn.sh
stop-dfs.sh

start-dfs.sh
start-yarn.sh
```

* Pour une première utilisation il faut configurer Hive

```
schematool -initSchema -dbType derby
```

* Lancer le serveur Hive

`hive --service hiveserver2`

* Lancer le client Hive

`hive --service beeline -u jdbc:hive2://localhost:10000`
