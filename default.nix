{ pkgs ? import <nixpkgs> {} }:

let
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
