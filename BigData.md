# Big Data

## Introduction

L'objectif est de manipuler des données ayant des propriéts particulières :
* Grande taille (lecture traffic RATP journalier)
* Grand debit (info provenant de la securité sociale)
* Grand nombre de formats (fichier de log)

Le big data se défini par le volume, la vélocité, la variabilité. Historiquement `Google` a fournit une solution de stockage : `GFS` et ubuntu aussi via : `CEPH`

Possibilité de stocker des données sur plusieurs machines => découpage des données en blocs

GFS est racheté par apache qui le renomme en HDFS (Hadoop Distributed Files System)

HDFS :
* NameNode : il connaît les url des blocs.
* SecondaryNameNode : il découpe un fichier en blocs (par blocs de 64Mo / Généralement en 128Mo)
* DataNode : il stocke les blocs.

PHOTO

Quand on installe un système de fichiers distribués cela signifie généralement sur plusieurs noeud de cluster Big Data (Groupe de machines)

PHOTO

Exemple de commande HDFS : `hdfs dfs -put exemple.csv /user/hduser`
Inconveient : Si la `JVM` du NameNode s'arrête alors les données sont perdues. Il faut mettre en place une stratégie de haute disponibilité (Fédération de NameNode)

HDFS possède 3 modes d'accès :
* Shell de commandes : ecrire les commandes, on peut scripter des opérations de calcul.
* API REST : les commandes HDFS sont associées à des requêtes HTTP.
* API JAVA : ensemble de closes pour développer de nouvelle  applications JAVA.

## Installation de Hadoop

[Site officiel Hadoop](https://hadoop.apache.org/)

* Installer un jdk

```
sudo tar xvzf jdk.tar.gz /opt/
```

* Créer un utilisteur et un groupe

```
sudo addgroup hadoop
sudo adduser --ingroup hadoop hduser
```

* Connecter 2 OS

```  
Sudo apt-get install openssh-server
```

* Créer une paire de clés avec RSA

```
ssh-keygen -t rsa -P ””
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
```

Vérifier la configuration :

```
ssh location
```

* Installer Apache Hadoop

http://mirror.ibcp.fr/pub/apache/hadoop/common/hadoop-2.7.6/

```
Sudo Tar xzf hadoop-2.7.4.tgz -C /opt/.
Sudo chown hduser:hadoop -R /opt/hadoop-2.7.4
```

* Créer 2 répertoires pour NameNode et la DataNode

```
sudo mkdir -p /var/hadoop/hdfs/namenode
sudo mkdir -p /var/hadoop/hdfs/datanode
sudo chown hduser:hadoop -R /var/hadoop
```

* Configuration utilisateur

```
export JAVA_HOME=/opt/jdk1.8.0
```

NB : il est préférable de modifier le fichier .bashrc de l'utilisateur.

* Configuration Hadoop

Modifier core-site.xml
```
<configuration>
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://localhost:9000</value>
    </property>
</configuration>
```

Modifier hdfs-site.xml
```
<configuration>
    <property>
        <name>dfs.replication</name>
        <value>1</value>
    </property>
    <property>
        <name>dfs.namenode.name.dir</name>
        <value>/var/hadoop/namenode</value>
    </property>
    <property>
        <name>dfs.datanode.name.dir</name>
        <value>/var/hadoop/datanode</value>
    </property>
</configuration>
```

* Formatage de la partition distribuée

```
export JAVA_HOME=/opt/jdk1.8.0
export HADOOP_HOME=/opt/hadoop
export PATH=$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$PATH

source ~/.bashrc

hdfs namenode -format
```

* Lancer HDFS

`start-dfs.sh`

Utiliser `jps` pour voir les jvm en cours. Normalement il devrait y avoir le namenode, le datanode et le secondarynamenode.

* Valider HDFS

Lancer un navigateur et taper http://localhost:50070/\. C'est l'url de l'interface web de HDFS.

Pour stopper HDFS taper `stop-dfs.sh`

## Utilisation de Hadoop

### HDFS CLI (command line interface)
C'est un shell de commande sur une machine du cluster :  
Comme le PATH contient $HADOOP/bin alors la commande hdfs est reconnue. Toutes les commandes du shell débute par **hdfs dfs -directive parametres**

* Créer un répertoire dans HDFS : `Hdfs dfs -mkdir -p /user/hduser/seance2`

* Importer un fichier depuis le système local dans HDFS : `Hdfs dfs -copyFromLocal  <file.csv> /user/hduser/seance2`  
Lors de l’installation  un group hadoop a été créer, un utilisateur hduser a été créer.

Les systèmes de droit d’accès aux fichiers sont différentes entre le système local et HDFS. Il faut que chaque compte appartiennent à son utilisateur.

* changer les droits : `Hdfs dfs -chown -R hduser:hadoop  /user/hduser` et `Hdfs dfs -chmod -R 744  /user/hduser`

### API JAVA

PHOTO

* L'utilisation de cette API est idéal pour retourner un état suite à une requête http

`Http` est un langage outil d’observation de requête http : tcpmon wireshark  
NB : en http les verbes get et delete n’ont pas de body dans la requête  
Pour effectuer des appel via des API rest il faut télécharger postman ou google Chrome directement.

* Pour utiliser l’API REST d’HDFS, il faut mofifier le fichier `hdfs-site.xml` :
```
<configuration>
    <property>
        <name>dfs.webhdfs.enabled</name>
        <value>true</value>
    </property>
```

* Restart le service hdfs :
```
stop-dfs.sh
start-dfs.sh
```

* Avec postman créer un répertoire seance2
```
Put http://localhost:50070//webhdfs/v1/user/hduser/seance2?op=MKDIRS&user.name=hduser
```
* lire les infos sur un répertoire
```
Get http://localhost:50070//webhdfs/v1/user/hduser/seance2?op=GETFILESTATUS
```
Add : Accept/application/json to the header.

* Lire un fichier info:csv présent dans le répertoire seance2
```
Get http://localhost:50070//webhdfs/v1/user/hduser/seance2/info:csv?user.name=hduser&op=OPEN
```
Add : Accept/application/xml to the header

* Supprimer le fichier info:csv
```
Delete http://localhost:50070//webhdfs/v1/user/hduser/seance2?user.name=hduser&op=DELETE
```
Add : Accept/application/xml to the header


`Hadoop` est écrit en Java => l’API de développement est en Java  
Hadoop-common: la librairie d’I/O  
Hadoop-hdfs : la librairie d’accès à FileSystem

Pour construire un programme, il faut un IDE (Eclipse,Intelli J,), un outil d’automatisation (Maven,Gradle)

Exemple :
```JAVA
Package seance2;

import org.apache.io.*;

Public class ListerRepertoire {

    Public static void main (String [] args) {
        if (args.length == 1)
            Path repertoire = new Path(args[0]);
        Configuration config = new Configuration();
        config.addRessource('opt/hadoop-2.9.7/etc/hadoop/hdfs-site.xml');
        config.addRessource('opt/hadoop-2.9.7/etc/hadoop/core-site.xml');
        FileSystem hdfs = FileSystem.get(config);
        FileStatus[]  files = hdfs.listStatus(repertoire);
        for (FileStatus status : files) {
            system.out.println(status.getPath().getName());
        }
    }
}
```
