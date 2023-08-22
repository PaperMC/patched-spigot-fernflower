plugins {
  `maven-publish`
  id("ca.stellardrift.gitpatcher") version "1.0.0"
}

val ghRunNumber = System.getenv("GITHUB_RUN_NUMBER")?.let { "build.$it" } ?: "local"
group = "io.papermc"
version = "0.1+$ghRunNumber"

patches {
  submodule.set("work/Fernflower")
  target.set(file("patched-spigot-fernflower"))
  patches.set(file("patches"))
}

tasks.register("rebuildPatches") {
  dependsOn(tasks.makePatches)
}

val ff = tasks.register<Exec>("buildFernflower") {
  doNotTrackState("Always run when requested")
  outputs.file(layout.projectDirectory.file("fernflower.jar"))
  executable = layout.projectDirectory.file("build.sh").asFile.absolutePath
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
