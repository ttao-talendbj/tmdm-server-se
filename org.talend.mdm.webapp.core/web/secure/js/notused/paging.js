/*
 * Ext JS Library 2.0
 * Copyright(c) 2006-2007, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

Ext.onReady(function(){

		var recordType = Ext.data.Record.create([
		  {name: "id", type: "int"},
		  {name: "description", type: "string"}
		  ]);


		  
    // create the Data Store
    var store = new Ext.data.Store({
        // load using script tags for cross domain, if the data in on the same domain as
        // this page, an HttpProxy would be better
	    proxy: new Ext.data.DWRProxy(LayoutInterface.getItems, false),
	    reader: new Ext.data.ListRangeReader( 
				{id:'id', totalProperty:'totalSize'}),
	    remoteSort: false
    });
    //store.setDefaultSort('lastpost', 'desc');

    // pluggable renders
/*    function renderTopic(value, p, record){
        return String.format(
                '<b><a href="http://extjs.com/forum/showthread.php?t={2}" target="_blank">{0}</a></b><a href="http://extjs.com/forum/forumdisplay.php?f={3}" target="_blank">{1} Forum</a>',
                value, record.data.forumtitle, record.id, record.data.forumid);
    }
   
    function renderLast(value, p, r){
        return String.format('{0}<br/>by {1}', value.dateFormat('M j, Y, g:i a'), r.data['lastposter']);
    } */

    // the column model has information about grid columns
    // dataIndex maps the column to the specific data field in
    // the data store
    var cm = new Ext.grid.ColumnModel([					
    {
						header: 'Description',
						width: 250,
						sortable: true,
						dataIndex: 'description',
					},
					{
						header: 'First Name',
						width: 250,
						sortable: true,
						dataIndex: 'first',
					},
					{
						header: 'Last Name',
						width: 250,
						sortable: true,
						dataIndex: 'last',
					}	]);

    // by default columns are sortable
    cm.defaultSortable = true;

    var grid = new Ext.grid.GridPanel({
        el:'topic-grid',
        width:700,
        height:500,
        title:'ExtJS.com - Browse Forums',
        store: store,
        cm: cm,
        trackMouseOver:false,
        sm: new Ext.grid.RowSelectionModel({selectRow:Ext.emptyFn}),
        loadMask: true,
        viewConfig: {
            forceFit:true,
            enableRowBody:true,
            showPreview:true,
            getRowClass : function(record, rowIndex, p, store){
                if(this.showPreview){
                    p.body = '<p>'+record.data.excerpt+'</p>';
                    return 'x-grid3-row-expanded';
                }
                return 'x-grid3-row-collapsed';
            }
        },
        bbar: new Ext.PagingToolbar({
            pageSize: 25,
            store: store,
            displayInfo: true,
            displayMsg: 'Displaying topics {0} - {1} of {2}',
            emptyMsg: "No topics to display",
            items:[
                '-', {
                pressed: true,
                enableToggle:true,
                text: 'Show Preview',
                cls: 'x-btn-text-icon details',
                toggleHandler: toggleDetails
            }]
        })
    });

    // render it
    grid.render();

    // trigger the data store load
    //store.load({params:{start:0, limit:25}});
store.load({params:{start:0, limit:22}, arg:['walter', true,0,25,'ASC']});
    function toggleDetails(btn, pressed){
        var view = grid.getView();
        view.showPreview = pressed;
        view.refresh();
    }
});