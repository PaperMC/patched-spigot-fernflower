import net.minecrell.gitpatcher.PatchExtension

plugins {
  id("net.minecraftforge.gitpatcher") version "0.10.+"
}

configure<PatchExtension> {
  submodule = "work/Fernflower"
  target = file("patched-spigot-fernflower")
  patches = file("patches")
}

tasks.register("rebuildPatches") {
  dependsOn(tasks.makePatches)
}
