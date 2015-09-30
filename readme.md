Ausführen
---------

sbt run


Beschreibung
------------

Es öffnen sich sechs "Konsolen": Zwei Instanzen der Hauptanwendung und zwei Instanzen der mobilen Anwendung, für letztere jeweils zusätzlich eine Sensor-Konsole.

Möglicher Ablauf:

* H1: Erste Hauptanwendung
* T1: Erstes Tablet
* S1: Näherungssensor des ersten Tablets

Akteuer | Befehl | Bedeutung
-----------------------------
H1      | login at Besprechungsraum | Teamleiter (TL) meldet sich an 
H1      | load projects             | TL lädt Projektübersicht
S1      | go to desk at Besprechungsraum | Erster Projektverantwortlicher (PV) tritt ans Pult
H1      | open project 1                 | PV öffnet sein erstes Projekt (wird auf Tablet synchronisiert)
H1      | open project 2                 | PV öffnet sein zweites Projekt (wird auf Tablet synchronisiert)
T1      | remember        | Projekt vormerken um später eine Maßnahme einzutragen
S1      | leave desk      | Erster PV ist fertig
S2      | go to desk at Besprechungsraum | Zweiter PV tritt ans Pult
H1      | open project 3  | Zweiter PV öffnet sein Projekt (wird auf Tablet synchronisiert)
T1      | edit 2          | Erster PV beginnt unterdessen mit dem Anlegen der Maßnahme
H1      | logout          | TL beendet Reko
T1      | save            | Erster PV schließt Anlegen der Maßnahme ab

