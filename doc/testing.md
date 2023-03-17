---
title: Testing
---

Datum: 17. März 2023

Testperson: Micha Baumann

| Testfall             | Vorbedingungen                              | Schritte                                   | Erwartet                                                                                   | OK? |
|----------------------|---------------------------------------------|--------------------------------------------|--------------------------------------------------------------------------------------------|-----|
| Welcome Screen       | Die App wird das erste Mal gestartet        | 1. Klick auf Button 2. Permission zulassen | Nach Akzeptanz der Permission wird die Karte geöffnet                                      | X   |
| Karte bewegen        | Die Karte wurde geladen                     | 1. Karte mit dem Finger bewegen            | Die Karte und der Trace bewegen sich entsprechend.                                         | X   |
| Karte zoomen         | Die Karte wurde geladen                     | 1. Karte mit zwei Fingern zoomen           | Die Karte und der Trace werden entsprechend vergrössert/verkleinert.                       | X   |
| Tracing aktivieren   | Permissions zugelassen, Tracing deaktiviert | 1. Auf den Switch tippen                   | Das Tracing wird aktiviert. Die Service-Benachrichtigung erscheint nach ein paar Sekunden. | X   |
| Tracing deaktivieren | Permissions zugelassen, Tracing aktiviert   | 1. Auf den Switch tippen                   | Das Tracing wird deaktiviert. Die Service-Benachrichtigung verschwindet.                   | X   |
