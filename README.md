# SETUP

1. Setup Scala using Coursier: https://get-coursier.io/docs/cli-installation#native-launcher

```bash
curl -fL "https://github.com/coursier/launchers/raw/master/cs-x86_64-pc-linux.gz" | gzip -d > cs
chmod +x cs
./cs # or ./cs setup

# Fedora
sudo dnf install java-17-openjdk-src # downloading java-17-sdk src.zip for Goto. defns. etc
# Ubuntu/Debian
sudo apt install java-17-openjdk-src
```

- add the `coursier` directory to `$PATH` (probably ~/.local/share/coursier/bin/)
  - (bash) — `export PATH=$PATH:<COURSIER_DIRECTORY>/bin`
  - (fish) — `fish_add_path ~/.local/share/coursier/bin`
  
- add `JAVA_HOME` environment variable to shell config (`.bashrc`, `.fishrc`) e.g
  - (bash) - `export JAVA_HOME="/usr/lib/jvm/java-17-openjdk-17.0.12.0.7-2.fc40.x86_64"`
  - (fish) - `set -x JAVA_HOME "/usr/lib/jvm/java-17-openjdk-17.0.12.0.7-2.fc40.x86_64"`

2. Scala quickstarts:

- https://learnxinyminutes.com/scala/
- https://docs.scala-lang.org/ | https://docs.scala-lang.org/toolkit/introduction.html | 
- https://docs.scala-lang.org/scala3/book/scala-for-python-devs.html
- https://dev.to/josethz00/learn-scala-in-5-minutes-12cc
- https://jeoygin.gitbooks.io/learn-y-in-x-minutes/content/scala.html
- https://youtu.be/I7-hxTbpscU?feature=shared

3. Setup Postgres database with JDBC connector

- This project uses AzureSQL and Postgres from Azure

4. Cheatsheet
   1. Scala in 100 Seconds <https://youtu.be/I7-hxTbpscU?feature=shared>
   2. Sending HTTP Requests <https://docs.scala-lang.org/toolkit/http-client-intro.html>
   3. Handling JSON <https://docs.scala-lang.org/toolkit/json-intro.html>
   4. Building Web Servers <https://docs.scala-lang.org/toolkit/web-server-intro.html>
   5. 