REM Based on http://astrofloyd.wordpress.com/2012/09/06/convert-bzr-to-git/

cd ..
mv Java Java-bzr
md Java
cd Java
cp ../Java-bzr/.bzrignore .gitignore

REM Initialise a new git repo
git init
REM Do the actual conversion
bzr fast-export --plain ../Java-bzr | git fast-import

REM Must do git add of the files again, otherwise they are marked as deleted (?)

REM git commit -m "Converted from Bazaar to Git"
