App tests, migrations, updates, android versions
=====================
Created by james on 2019-05-07

This is an executable specification file which follows markdown syntax.
Every heading in this file denotes a scenario. Every bulleted point denotes a step.
     
//## Ensure the app can be upgraded without uninstalling from previous versions
//---------------
//tags: Smoke

    //* App: Install previous version
    //* App: Launch
    //* Boulder List: Ready?
    //* App: Close
    //* App: Install new version
    //* App: Launch
    //* Boulder List: Ready?
