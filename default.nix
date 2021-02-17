{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  shellHook = ''
  '';

  buildInputs = with pkgs; [
    cacert
    coreutils-full
    curlFull
    glibcLocales 
    jdk
    sbt
    busybox # always last
  ];
}
