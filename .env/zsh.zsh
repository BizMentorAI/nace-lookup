save-function-list

load ~/.zsh/environments/basic.zsh

alias emacs="emacs --load $PWD/.env/emacs.el"
path-prepend $PWD/.env/bin

export ET_JAR=~/Documents/Projects/et/target/et.jar
alias et="java -jar $ET_JAR"

function recreate-wip-branch() {
  # TODO: ask whether you merged in first, Y/n.
  if test wip = $(git branch | grep -E '^\*' | cut -c 3-); then
    git stash
    git checkout rel
    git pull -r
    git branch -D wip
    git co -b wip
    git stash pop
  fi
}

report-custom-functions
