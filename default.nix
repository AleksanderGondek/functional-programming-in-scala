{ }:

let
  pkgs = import (
    fetchTarball { url = https://github.com/NixOS/nixpkgs/archive/nixos-20.09.tar.gz;}
  ) {};
  name = "fp-in-scala-shell";
in
pkgs.mkShell {
  inherit name;

  shellHook = ''
    echo 'Welcome to ${name}!'
  '';

  buildInputs = with pkgs; [
    cacert
    coreutils-full
    curlFull
    glibcLocales 
    jdk
    mkdocs
    sbt
    busybox # always last
  ];
}
