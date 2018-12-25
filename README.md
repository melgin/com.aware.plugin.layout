AWARE: Layout
==========================

Layout is a plugin for [AWARE Framework](http://www.awareframework.com/). This plugin collects data about screen layout in a tree hierarchy whenever the screen layout changes.

# Settings
Parameters adjustable on the dashboard and client:
- **status_plugin_layout**: (boolean) enable/disable plugin

# Providers
##  Layout Data
> content://com.aware.plugin.layout.provider.layout/plugin_layout

Field | Type | Description
----- | ---- | -----------
_id | INTEGER | primary key auto-incremented
timestamp | REAL | unix timestamp in milliseconds of sample
device_id | TEXT | AWARE device ID
package | TEXT | Package name of the current app
dom_tree| TEXT | Screen layout as DOM tree
