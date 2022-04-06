import net.minecrell.gitpatcher.PatchExtension

plugins {
  `maven-publish`
  id("net.minecraftforge.gitpatcher") version "0.10.+"
}

val ghRunNumber = System.getenv("GITHUB_RUN_NUMBER")?.let { "build.$it" } ?: "local"
group = "io.papermc"
version = "0.1+$ghRunNumber"

configure<PatchExtension> {
  submodule = "work/Fernflower"
  target = file("patched-spigot-fernflower")
  patches = file("patches")
}

tasks.register("rebuildPatches") {
  dependsOn(tasks.makePatches)
}

val ff = tasks.register("buildFernflower") {
  doNotTrackState("Always run when requested")
  outputs.file(layout.projectDirectory.file("fernflower.jar"))
  doLast {
    exec {
      executable = layout.projectDirectory.file("build.sh").asFile.absolutePath
    }
  }
}

publishing {
  repositories.maven("https://papermc.io/repo/repository/maven-releases/") {
    name = "paper"
    credentials(PasswordCredentials::class)
  }
  publications.create<MavenPublication>("maven") {
    artifact(ff)
  }
}
