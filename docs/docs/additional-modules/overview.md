# Overview

ScrewBox currently provides just one additional module ([Tiled Editor](./tiled-editor)).
Maybe there will be other modules in the future.
Splitting dependencies is considered good practice to reduce resource allocations such as memory usage, bundle sizes,
and build times.

You can use the `screwbox-bom` to manage all ScrewBox dependencies.
This ensures using compatible versions of the ScrewBox libraries.

The Maven setup for a project using Tiled Editor support might look like:

``` xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>dev.screwbox</groupId>
            <artifactId>screwbox</artifactId>
            <version><!-- your version --></version>
            <packaging>pom</packaging>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>dev.screwbox</groupId>
        <artifactId>screwbox-core</artifactId>
    </dependency>
    
    <!-- optional additional dependencies -->
    <dependency>
        <groupId>dev.screwbox</groupId>
        <artifactId>screwbox-tiled</artifactId>
    </dependency>
</dependencies>
```