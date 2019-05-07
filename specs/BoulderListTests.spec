# Boulder List Tests

This is an executable specification file. This file follows markdown syntax.
Every heading in this file denotes a scenario. Every bulleted point denotes a step.

To execute this specification, run
	gauge run specs


* App: Launch

## App ready
---------------
tags: Smoke

    * Boulder List: Ready?
    
    
## Applying a filter should update the list correctly
---------------
tags: Smoke

    * Boulder List: Ready?
    * Boulder List: Apply a "V2" boulder grade filter
    * Boulder List: Ensure only boulders with the applied boulder grade filters appear in the list

## Applying multiple filters should update the list correctly
---------------
tags: Smoke

    * Boulder List: Ready?
    * Boulder List: Apply a "V2" boulder grade filter
    * Boulder List: Apply a "V4" boulder grade filter
    * Boulder List: Ensure only boulders with the applied boulder grade filters appear in the list


//## Removing filters should update the list correctly each time a filter is removed
//---------------
//tags:Smoke

    //* Boulder List: Ready?




_____

* App: Close