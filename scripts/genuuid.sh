#!/usr/bin/env sh

uuidgen | tr '[:upper:]' '[:lower:]' | tr -d '\n' | pbcopy
