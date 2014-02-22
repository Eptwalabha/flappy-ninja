Flappy NINJA!
=====
Flappy-bird (ou piou-piou vs cactus) like en JAVA avec le framework Artemis et Slick2D

## Flappy NINJA!

Ce projet utilise le famework [Artemis](http://gamadu.com/artemis/ "Artemis Framework") pour la logique du jeu
et la librairie de jeu [Slick 2D](http://slick.ninjacave.com/ "slick2D") pour les graphiques.
Il a été réalisé dans un but de démonstration pour montrer la facilité de développement avec le framework Artemis

### Mise en place de Slick2D pour Intellij 12

*Attention, ces étapes ne doivent être réalisées qu'après le clonage de ce projet sur votre machine*
Pour faire fonctionner ce projet sur Intellij, veuillez suivre ces étapes:
 1. Télécharger [la librairie](http://slick.ninjacave.com/slick.zip "télécharger slick2D").
 2. Créez un répertoire "/slick2D" où vous le souhaitez sur votre machine.
 3. Extraire la librairie précédemment téléchargée dans le dossier /slick2D.
 4. Mettre à jour les dépendances Maven.
 5. Créer une nouvelle librairie:
   * appuyez sur Ctrl + Alt + Shift + S pour ouvrir le menu 'Project Structure'
   * cliquez sur 'Libraries' puis créez une nouvelle librairie en sélectionnant le dossier '/slick2D/lib'
   * ajouter cette librairie à votre projet (Intellij vous le propose automatiquement, sinon allez dans l'onglet module puis ajoutez la librairie avec le bouton +)
 6. Renseignez le point d'entrée de l'application ainsi que le dossier des librairies systèmes:
  *Flappy NINJA!
=====

Flappy-bird (ou piou-piou vs cactus) like en JAVA avec le framework Artemis et Slick2D

## Flappy NINJA!

Ce projet utilise le famework [Artemis](http://gamadu.com/artemis/ "Artemis Framework") pour la logique du jeu
et la librairie de jeu [Slick 2D](http://slick.ninjacave.com/ "slick2D") pour les graphiques.

Il a été réalisé dans un but de démonstration pour montrer la facilité de développement avec le framework Artemis

### Mise en place de Slick2D pour Intellij 12

*Attention, ces étapes ne doivent être réalisées qu'après le clonage de ce projet sur votre machine*
Pour faire fonctionner ce projet sur Intellij, veuillez suivre ces étapes:
 1. Télécharger [la librairie](http://slick.ninjacave.com/slick.zip "télécharger slick2D").
 2. Créez un répertoire "/slick2D" où vous le souhaitez sur votre machine.
 3. Extraire la librairie précédemment téléchargée dans le dossier /slick2D.
 4. Mettre à jour les dépendances Maven.
 5. Créer une nouvelle librairie:
    * appuyez sur Ctrl + Alt + Shift + S pour ouvrir le menu 'Project Structure'
    * cliquez sur 'Libraries' puis créez une nouvelle librairie en sélectionnant le dossier `/slick2D/lib`
    * ajouter cette librairie à votre projet (Intellij vous le propose automatiquement, sinon allez dans l'onglet module puis ajoutez la librairie avec le bouton +)
 6. Renseignez le point d'entrée de l'application ainsi que le dossier des librairies systèmes:
    * Allez sur 'Run' puis 'Edit Configurations'
    * Cliquez sur le '+' pour ajouter une configuration de type 'Application'
    * Dans 'Main Class' entrez `Launcher`
    * Dans 'Vm Options' ajoutez cette ligne `-Djava.library.path=<le chemin vers le dossier slick2D>`

Le projet est configuré devrait maintenant pouvoir fonctionner sur vos machines.
 Allez sur 'Run' puis 'Edit Configurations'
  * Cliquez sur le '+' pour ajouter une configuration de type 'Application'
  * Dans 'Main Class' entrez 'Launcher'
  * Dans 'Vm Options' ajoutez cette ligne '-Djava.library.path=<le chemin vers le dossier slick2D>'

Le projet est configuré devrait maintenant pouvoir fonctionner sur vos machines.