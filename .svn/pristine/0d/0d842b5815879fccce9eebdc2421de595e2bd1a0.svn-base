(function($,sr){
    var debounce = function (func, threshold, execAsap) {
      var timeout;

        return function debounced () {
            var obj = this, args = arguments;
            function delayed () {
                if (!execAsap)
                    func.apply(obj, args); 
                timeout = null; 
            }

            if (timeout)
                clearTimeout(timeout);
            else if (execAsap)
                func.apply(obj, args);

            timeout = setTimeout(delayed, threshold || 100); 
        };
    };

    // smartresize 
    jQuery.fn[sr] = function(fn){  return fn ? this.bind('resize', debounce(fn)) : this.trigger(sr); };

})(jQuery,'smartresize');


var CURRENT_URL = window.location.href.split('#')[0].split('?')[0],
    $BODY = $('body'),
    $MENU_TOGGLE = $('#menu_toggle'),
    $SIDEBAR_MENU = $('#sidebar-menu'),
    $SIDEBAR_FOOTER = $('.sidebar-footer'),
    $LEFT_COL = $('.left_col'),
    $RIGHT_COL = $('.right_col'),
    $NAV_MENU = $('.nav_menu'),
    $FOOTER = $('footer');

	
	
// Sidebar
function init_sidebar() {
	var setContentHeight = function () {
		// reset height
		$RIGHT_COL.css('min-height', $(window).height());

		var bodyHeight = $BODY.outerHeight(),
			footerHeight = $BODY.hasClass('footer_fixed') ? -10 : $FOOTER.height(),
			leftColHeight = $LEFT_COL.eq(1).height() + $SIDEBAR_FOOTER.height(),
			contentHeight = bodyHeight < leftColHeight ? leftColHeight : bodyHeight;

		// normalize content
		contentHeight -=  footerHeight;

		$RIGHT_COL.css('min-height', (contentHeight -75));
	};

  	$SIDEBAR_MENU.find('a').on('click', function(ev) {
        var $li = $(this).parent();
        $(".custom_bg").height($(window).height());
        if ($li.is('.active')) {
            $li.removeClass('active active-sm');
            $('ul:first', $li).slideUp(function() {
                setContentHeight();
            });
        } else {
            // prevent closing menu if we are on child menu
            if (!$li.parent().is('.child_menu')) {
                $SIDEBAR_MENU.find('li').removeClass('active active-sm');
                $SIDEBAR_MENU.find('li ul').slideUp();
            }else{
				if ( $BODY.is( ".nav-sm" ) )
				{
					$SIDEBAR_MENU.find( "li" ).removeClass( "active active-sm" );
					$SIDEBAR_MENU.find( "li ul" ).slideUp();
				}
			}
            $li.addClass('active');

            $('ul:first', $li).slideDown(function() {
                setContentHeight();
            });
        }
    });

	// toggle small or large menu 
	$MENU_TOGGLE.on('click', function() {
			console.log('clicked - menu toggle');

			if ($BODY.hasClass('nav-md')) {
				$SIDEBAR_MENU.find('li.active ul').hide();
				$SIDEBAR_MENU.find('li.active').addClass('active-sm').removeClass('active');
			} else {
				$SIDEBAR_MENU.find('li.active-sm ul').show();
				$SIDEBAR_MENU.find('li.active-sm').addClass('active').removeClass('active-sm');
			}

		$BODY.toggleClass('nav-md nav-sm');

		setContentHeight();
	});

	// check active menu
	$SIDEBAR_MENU.find('a[href="' + CURRENT_URL + '"]').parent('li').addClass('current-page');

	$SIDEBAR_MENU.find('a').filter(function () {
		return this.href == CURRENT_URL;
	}).parent('li').addClass('current-page').parents('ul').slideDown(function() {
		setContentHeight();
	}).parent().addClass('active');

	// recompute content when resizing
	$(window).smartresize(function(){  
		setContentHeight();
	});

	setContentHeight();

	// fixed sidebar
	if ($.fn.mCustomScrollbar) {
		$('.menu_fixed').mCustomScrollbar({
			autoHideScrollbar: true,
			theme: 'minimal',
			mouseWheel:{ preventDefault: true }
		});
	}
};
//Sidebar
$(document).ready(function() {
	// Tooltip
	$('[data-toggle="tooltip"]').tooltip({
        container: 'body'
    });
    // Accordion
    $(".expand").on("click", function () {
        $(this).next().slideToggle(200);
        $expand = $(this).find(">:first-child");
        if ($expand.text() == "+") {
            $expand.text("-");
        } else {
            $expand.text("+");
        }
    });
    init_sidebar();
});


// //NAVIGATE LIST BY UP DOWN KEYS
// $(window).keydown(function(event){
//     var charKey = event.which || event.keyCode;
//     // CHARACTER KEY PRESS
//     if (charKey==100 || charKey==68) {                                                              //for d,D
//         if($('#controlpanel').hasClass('active') && $('#dashboard').hasClass('active') == false){ 	//CONTROL PANEL > DISPLAY SETUP
//             $("#displaysetup").addClass('active');
//             $('#displaysetup').siblings().removeClass('active');
//         }else if($('#dashboard').hasClass('active') == false){ 										//DASHBOARD
//             $('#dashboard').addClass('active');
//             $('#dashboard').siblings().removeClass('active');
//
//             $('#helpNav').css({'display': 'none'});
//             $('#taxNav').css({'display':'none'});
//             $('#generaltransactionNav').css({'display':'none'});
//             $('#controlpanelNav').css({'display':'none'});
//             $('#contactNav').css({'display':'none'});
//             $('#reportsNav').css({'display':'none'});
//         }
//     }else if(charKey==83 || charKey==115){                                                          //for s,S SALES
//         if($("#contact").hasClass('active') && $('#sales').hasClass('active') == false){ 			//CONTACT > SUPPLIER
//             $("#supplier").addClass('active');
//             $('#supplier').siblings().removeClass('active');
//         }else if($('#controlpanel').hasClass('active') && $('#sales').hasClass('active') == false ){  //CONTROL PANEL > COMPANY SETUP
//             $("#setup").addClass('active');
//             $('#setup').siblings().removeClass('active');
//         }else if($("#reports").hasClass('active') && $('#sales').hasClass('active') == false) {  	 //PURCHASE > SALES
//             $("#reportsales").addClass('active');
//             $('#reportsales').siblings().removeClass('active');
//         }else if($('#help').hasClass('active') && $('#sales').hasClass('active') == false){ 		//HELP > MANUAL
//             $('#subscription').addClass('active');
//             $('#subscription').siblings().removeClass('active');
//         }else if($('#sales').hasClass('active') == false) { 										//SALES
//             $('#sales').addClass('active');
//             $('#sales').siblings().removeClass('active');
//
//             $('#helpNav').css({'display': 'none'});
//             $('#taxNav').css({'display':'none'});
//             $('#generaltransactionNav').css({'display':'none'});
//             $('#controlpanelNav').css({'display':'none'});
//             $('#contactNav').css({'display':'none'});
//             $('#reportsNav').css({'display':'none'});
//         }
//     }else if(charKey==112 || charKey==80){                                                          //for p,P
//         if($("#reports").hasClass('active') && ($('#purchase').hasClass('active') == false)){  		//REPORTS > PURCHASE
//             $("#reportpurchase").addClass('active');
//             $('#reportpurchase').siblings().removeClass('active');
//         }else if($('#purchase').hasClass('active') == false){                                       //PURCHASE
//             $('#purchase').addClass('active');
//             $('#purchase').siblings().removeClass('active');
//
//             $('#helpNav').css({'display': 'none'});
//             $('#taxNav').css({'display':'none'});
//             $('#generaltransactionNav').css({'display':'none'});
//             $('#controlpanelNav').css({'display':'none'});
//             $('#contactNav').css({'display':'none'});
//             $('#reportsNav').css({'display':'none'});
//         }
//     }else if(charKey==103 || charKey==71){                                                          //for g,G
//         if($('#tax').hasClass('active') && $('#generaltransaction').hasClass('active') == false){ 	//TAX>GST
//             $('#gst').addClass('active');
//             $('#gst').siblings().removeClass('active');
//         }else if($('#generaltransaction').hasClass('active') == false){ 							//GENERAL TRANSACTION
//             $('#generaltransaction').addClass('active');
//             $('#generaltransaction').siblings().removeClass('active');
//
//             $('#helpNav').css({'display': 'none'});
//             $('#taxNav').css({'display':'none'});
//             $('#controlpanelNav').css({'display':'none'});
//             $('#contactNav').css({'display':'none'});
//             $('#reportsNav').css({'display':'none'});
//         }
//     }else if((charKey==101 || charKey==69) && ($('#generaltransaction').hasClass('active'))){ 		//for e,E GENERAL TRANS > EXPENSE
//         $("#expense").addClass('active');
//         $('#expense').siblings().removeClass('active');
//     }else if((charKey==114 || charKey==82)){                                                        //for r,R
//         if($('#generaltransaction').hasClass('active') && $('#reports').hasClass('active')==false){ //GENERAL TRANS > RECEIPT
//             $('#receipt').addClass('active');
//             $('#receipt').siblings().removeClass('active');
//         }else if($('#reports').hasClass('active')==false){ 											//REPORTS
//             $("#reports").addClass('active');
//             $('#reports').siblings().removeClass('active');
//
//             $('#helpNav').css({'display': 'none'});
//             $('#taxNav').css({'display':'none'});
//             $('#generaltransactionNav').css({'display':'none'});
//             $('#controlpanelNav').css({'display':'none'});
//             $('#contactNav').css({'display':'none'});
//         }
//     }else if((charKey==106 || charKey==74) && ($('#generaltransaction').hasClass('active'))){ 		//for j,J GENERAL TRANS > JOURNAL ENTRY
//         $('#journalentry').addClass('active');
//         $('#journalentry').siblings().removeClass('active');
//     }else if(charKey==105 ||charKey==73) {                                                          //for i,I
//         if($('#reports').hasClass('active') && $('#inventory').hasClass('active') == false){ 		//for r,R REPORTS > INVENTORY
//             $("#reportinventory").addClass('active');
//             $('#reportinventory').siblings().removeClass('active');
//         }else if ($('#inventory').hasClass('active') == false) { 									//INVENTORY
//             $('#inventory').addClass('active');
//             $('#inventory').siblings().removeClass('active');
//
//             $('#helpNav').css({'display': 'none'});
//             $('#taxNav').css({'display':'none'});
//             $('#generaltransactionNav').css({'display':'none'});
//             $('#controlpanelNav').css({'display':'none'});
//             $('#contactNav').css({'display':'none'});
//             $('#reportsNav').css({'display':'none'});
//         }
//     }else if(charKey==116 || charKey==84) {                                                         //for t,T
//         if ($('#tax').hasClass('active') == false) { 												//TAX
//             $('#tax').addClass('active');
//             $('#tax').siblings().removeClass('active');
//
//             $('#helpNav').css({'display': 'none'});
//             $('#generaltransactionNav').css({'display':'none'});
//             $('#controlpanelNav').css({'display':'none'});
//             $('#contactNav').css({'display':'none'});
//             $('#reportsNav').css({'display':'none'});
//         }else if($('#tax').hasClass('active')){ 													//TAX > TAX CONFIG
//             $('#taxconfiguration').addClass('active');
//             $('#taxconfiguration').siblings().removeClass('active');
//         }
//     }else if(charKey==99 || charKey==67) {                                                          //for c,C
//         if($("#contact").hasClass('active') && $('#controlpanel').hasClass('active') == false){ 	//CONTACT > CUSTOMER
//             $("#customer").addClass('active');
//             $('#customer').siblings().removeClass('active');
//         }else if($('#controlpanel').hasClass('active') && $("#contact").hasClass('active')==false){  //CONTROL PANEL > CONFIGURATOR
//             $("#configurator").addClass('active');
//             $('#configurator').siblings().removeClass('active');
//         }else if($('#controlpanel').hasClass('active') == false) { 									 //CONTROL PANEL
//             $('#controlpanel').addClass('active');
//             $('#controlpanel').siblings().removeClass('active');
//
//             $('#helpNav').css({'display': 'none'});
//             $('#generaltransactionNav').css({'display':'none'});
//             $('#taxNav').css({'display':'none'});
//             $('#contactNav').css({'display':'none'});
//             $('#reportsNav').css({'display':'none'});
//         }
//     }else if(charKey==111 || charKey==79){                                                          //for o,O
//         if($('#controlpanel').hasClass('active') && $("#contact").hasClass('active')==false){ 		//CONTROL PANEL > OPENING BALC
//             $("#openingbalance").addClass('active');
//             $('#openingbalance').siblings().removeClass('active');
//         }else if( $("#contact").hasClass('active')==false){ 										//CONTACT
//             $("#contact").addClass('active');
//             $('#contact').siblings().removeClass('active');
//
//             $('#helpNav').css({'display': 'none'});
//             $('#generaltransactionNav').css({'display':'none'});
//             $('#taxNav').css({'display':'none'});
//             $('#controlpanelNav').css({'display':'none'});
//             $('#reportsNav').css({'display':'none'});
//         }else if($('#contact').hasClass('active')){ 												//CONTACT > CONTACTS
//             $("#contacts").addClass('active');
//             $('#contacts').siblings().removeClass('active');
//         }
//     }else if((charKey==117 || charKey==85) && ($('#controlpanel').hasClass('active'))){ 			//for U,u CONTROL PANEL > USER ACCOUNT SETUP
//         $("#useraccountsetup").addClass('active');
//         $('#useraccountsetup').siblings().removeClass('active');
//     }else if((charKey==97 || charKey==65) && ($('#controlpanel').hasClass('active'))){ 				//for a,A CONTROL PANEL > ACCOUNT MAINTENANCE
//         $("#accountmaintenance").addClass('active');
//         $('#accountmaintenance').siblings().removeClass('active');
//     }else if((charKey==102 || charKey==70) && $('#reports').hasClass('active')){ 					//for F,f REPORTS > FINANCIAL STATE
//         $("#reportfinancialstatement").addClass('active');
//         $('#reportfinancialstatement').siblings().removeClass('active');
//     }else if((charKey==98 || charKey==66) && $('#reports').hasClass('active')){ 					//for r,R REPORTS > BANK RECON
//         $("#reportbankreconciliation").addClass('active');
//         $('#reportbankreconciliation').siblings().removeClass('active');
//     }else if(charKey==104 || charKey==72) {                                                         //for h,H
//         if( $("#help").hasClass('active')==false) { 										        //HELP
//             $("#help").addClass('active');
//             $('#help').siblings().removeClass('active');
//
//             $('#generaltransactionNav').css({'display': 'none'});
//             $('#taxNav').css({'display': 'none'});
//             $('#controlpanelNav').css({'display': 'none'});
//             $('#reportsNav').css({'display': 'none'});
//             $('#contactNav').css({'display': 'none'});
//         }else if($('#help').hasClass('active')){ 													//HELP > MANUAL
//             $('#manual').addClass('active');
//             $('#manual').siblings().removeClass('active');
//         }
//     }
//
//     //ENTER KEY PRESS
//     if(charKey==13) {
//         //alert('enter');
//         var $li = $("#sidebar-menu a").parent();
//         if($('ul li', $li).hasClass('active')==false) {
//             $('ul li:first', $li).addClass('active');
//         }
//         if($('#dashboard').hasClass('active')){
//             $('#dashboardLink').click();
//         }else if($('#sales').hasClass('active')){
//             $('#salesLink').click();
//         }else if($('#purchase').hasClass('active')){
//             $('#purchaseLink').click();
//         }else if($('#generaltransaction').hasClass('active')){ //GENERAL TRANSACTION
//             $('#generaltransactionNav').css({'display':'block'});
//             if($("#expense").hasClass('active')) {
//                 $("#expenseLink").click();
//             }else if($("#receipt").hasClass('active')) {
//                 //$("#expense").removeClass('active');
//                 $('#receiptLink').parent().siblings().removeClass('active');
//                 $('#receiptLink').click();
//             }else if($("#journalentry").hasClass('active')) {
//                 //$("#expense").removeClass('active');
//                 $('#journalentryLink').parent().siblings().removeClass('active');
//                 $('#journalentryLink').click();
//             }
//         }else if($('#inventory').hasClass('active')){			//INVENTORY
//             $('#inventoryLink').click();
//         }else if($('#tax').hasClass('active')){					//TAX
//             $('#taxNav').css({'display':'block'});
//             if($("#taxconfiguration").hasClass('active')) {
//                 $('#taxconfigurationLink').click();
//             }else if($("#gst").hasClass('active')) {
//                 //$("#taxconfiguration").removeClass('active');
//                 $('#gstLink').parent().siblings().removeClass('active');
//                 $("#gstLink").click();
//             }
//         }else if($('#controlpanel').hasClass('active')){
//             $('#controlpanelNav').css({'display':'block'});
//             if($("#openingbalance").hasClass('active')){
//                 $("#openingbalanceLink").click();
//             }else if($("#configurator").hasClass('active')){
//                 //$("#openingbalance").removeClass('active');
//                 $('#configuratorLink').parent().siblings().removeClass('active');
//                 $("#configuratorLink").click();
//             }else if($("#setup").hasClass('active')){
//                 //$("#openingbalance").removeClass('active');
//                 $('#setupLink').parent().siblings().removeClass('active');
//                 $("#setupLink").click();
//             }else if($("#useraccountsetup").hasClass('active')){
//                 //$("#openingbalance").removeClass('active');
//                 $('#useraccountsetupLink').parent().siblings().removeClass('active');
//                 $("#useraccountsetupLink").click();
//             }else if($("#accountmaintenance").hasClass('active')){
//                 //$("#openingbalance").removeClass('active');
//                 $('#accountmaintenanceLink').parent().siblings().removeClass('active');
//                 $("#accountmaintenanceLink").click();
//             }else if($("#displaysetup").hasClass('active')){
//                 //$("#openingbalance").removeClass('active');
//                 $('#displaysetupLink').parent().siblings().removeClass('active');
//                 $("#displaysetupLink").click();
//             }
//         }else if($('#contact').hasClass('active')){
//             $('#contactNav').css({'display':'block'});
//             if($("#supplier").hasClass('active')){
//                 $("#supplierLink").click();
//             }else if($("#customer").hasClass('active')){
//                 //$("#supplier").removeClass('active');
//                 $('#customerLink').parent().siblings().removeClass('active');
//                 $("#customerLink").click();
//             }else if($("#contacts").hasClass('active')){
//                 //$("#supplier").removeClass('active');
//                 $('#contactsLink').parent().siblings().removeClass('active');
//                 $("#contactsLink").click();
//             }
//         }else if($('#reports').hasClass('active')){
//             $('#reportsNav').css({'display':'block'});
//             if($("#reportpurchase").hasClass('active')){
//                 $("#reportpurchaseLink").click();
//             }else if($("#reportsales").hasClass('active')){
//                 //$("#reportpurchase").removeClass('active');
//                 $('#reportsalesLink').parent().siblings().removeClass('active');
//                 $("#reportsalesLink").click();
//             }else if($("#reportinventory").hasClass('active')) {
//                 //$("#reportpurchase").removeClass('active');
//                 $('#reportinventoryLink').parent().siblings().removeClass('active');
//                 $("#reportinventoryLink").click();
//             }else if($("#reportfinancialstatement").hasClass('active')){
//                 //$("#reportpurchase").removeClass('active');
//                 $('#reportfinancialstatementLink').parent().siblings().removeClass('active');
//                 $("#reportfinancialstatementLink").click();
//             }else if($("#reportbankreconciliation").hasClass('active')){
//                 //$("#reportpurchase").removeClass('active');
//                 $('#reportbankreconciliationLink').parent().siblings().removeClass('active');
//                 $("#reportbankreconciliationLink").click();
//             }
//         }else if($('#help').hasClass('active')){
//             $('#helpNav').css({'display':'block'});
//             if($("#manual").hasClass('active')){
//                 $("#manualLink").click();
//             }else if($("#subscription").hasClass('active')){
//                 //$("#manual").removeClass('active');
//                 $('#subscriptionLink').parent().siblings().removeClass('active');
//                 $("#subscriptionLink").click();
//             }
//         }
//     }
//
//     //UP DOWN KEY PRESS
//     if(charKey === 40) {
//         var $current = $('#nav_side_menu ul li.active');
//         var $currentLi = $('#nav_side_menu ul li ul li.active');
//         if ($current.next().length > 0) {
//             if( $('#nav_side_menu ul li').hasClass('active') ){
//                 if( $('#nav_side_menu ul li ul li').hasClass('active') ){
//                     if ($currentLi.next().length > 0) {
//                         $('#nav_side_menu ul li ul li').removeClass('active');
//                         $currentLi.next().addClass('active');
//                     }
//                 }else{
//                     $('#nav_side_menu ul li').removeClass('active');
//                     $current.next().addClass('active');
//                 }
//             }else{
//                 //$('#nav_side_menu ul li').removeClass('active');
//                 $current.next().addClass('active');
//             }
//         }
//     }else if(charKey === 38) {
//         var $current = $('#nav_side_menu ul li.active');
//         var $currentLi = $('#nav_side_menu ul li ul li.active');
//         if ($current.prev().length > 0) {
//             if($('#nav_side_menu ul li').hasClass('active')){
//                 if( $('#nav_side_menu ul li ul li').hasClass('active') ){
//                     if ($currentLi.prev().length > 0) {
//                         $('#nav_side_menu ul li ul li').removeClass('active');
//                         $currentLi.prev().addClass('active');
//                     }
//                 }else{
//                     $('#nav_side_menu ul li').removeClass('active');
//                     $current.prev().addClass('active');
//                 }
//             }else{
//                 $current.prev().addClass('active');
//             }
//         }
//     }
//
// });

// window.addEventListener("keydown", function(e) {
//     if($('.modal').is(':visible')) {
//         // up and down keys
//         var charKey = e.which || e.keyCode;
//         if (charKey === 40 || charKey === 38) {
//                     alert('up down');
//                     e.preventDefault();
//                     e.stopPropagation();
//         }
//     }
// }, false);

