# simple script for compilation of sources, to check, if there are
# no errors in the project.
# will not create any resources.
# assumes, that compile.sh has run sucessfully
# in ../framework and ../swingBackend

rm -r classes
mkdir classes
javac -cp ../../framework/classes:../../swingBackend/classes:../../javafxBackend/classes -d classes src/name/panitz/game/klaus/*.java
java  -cp ../../framework/classes:../../swingBackend/classes:../../javafxBackend/classes:classes:resources name.panitz.game.klaus.PlayFX
