REM http://astrofloyd.wordpress.com/2012/09/06/convert-bzr-to-git/

mv Java Java-bzr
md Java
cd Java

REM Initialise a new git repo
git init
REM Do the actual conversion
bzr fast-export --plain ../Java-bzr | git fast-import

git commit -m "Converted from Bazaar to Git"
