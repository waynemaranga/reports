#

1. Setup Scala using Coursier: https://get-coursier.io/docs/cli-installation#native-launcher
```bash
curl -fL "https://github.com/coursier/launchers/raw/master/cs-x86_64-pc-linux.gz" | gzip -d > cs
chmod +x cs
./cs # or ./cs setup
```
- add the `coursier` directory to `$PATH` (probably ~/.local/share/coursier/bin/)
  - (bash) — `export PATH=$PATH`
  - (fish) — `fish_add_path ~/.local/share/coursier/bin/`
- add `JAVA_HOME` environment variable to shell config (`.bashrc`, `.fishrc`) e.g
  - (bash) - `export JAVA_HOME=""` 
  - (fish) - `set -x JAVA_HOME ""`

2. Scala quickstarts:
  - https://learnxinyminutes.com/scala/
  - https://docs.scala-lang.org/scala3/book/scala-for-python-devs.html
  - https://dev.to/josethz00/learn-scala-in-5-minutes-12cc
  - https://jeoygin.gitbooks.io/learn-y-in-x-minutes/content/scala.html
  - https://youtu.be/I7-hxTbpscU?feature=shared
