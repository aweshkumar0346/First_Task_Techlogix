/**
 * @license
 * Copyright (c) 2014, 2025, Oracle and/or its affiliates.
 * Licensed under The Universal Permissive License (UPL), Version 1.0
 * as shown at https://oss.oracle.com/licenses/upl/
 * @ignore
 */
/*
 * Your application specific code will go here
 */
define(['knockout', 'ojs/ojcontext', 'ojs/ojmodule-element-utils', 'ojs/ojknockouttemplateutils', 
        'ojs/ojcorerouter', 'ojs/ojmodulerouter-adapter', 'ojs/ojknockoutrouteradapter', 
        'ojs/ojurlparamadapter', 'ojs/ojresponsiveutils', 'ojs/ojresponsiveknockoututils', 
        'ojs/ojarraydataprovider', 'ojs/ojdrawerpopup', 'ojs/ojmodule-element', 'ojs/ojknockout'],
  function(ko, Context, moduleUtils, KnockoutTemplateUtils, CoreRouter, ModuleRouterAdapter, 
           KnockoutRouterAdapter, UrlParamAdapter, ResponsiveUtils, ResponsiveKnockoutUtils, ArrayDataProvider) {
     
     function ControllerViewModel() {
      var self = this;
      
      this.KnockoutTemplateUtils = KnockoutTemplateUtils;
      
      // Handle announcements sent when pages change, for Accessibility.
      this.manner = ko.observable('polite');
      this.message = ko.observable();
      
      var announcementHandler = (event) => {
          this.message(event.detail.message);
          this.manner(event.detail.manner);
      };
      document.getElementById('globalBody').addEventListener('announce', announcementHandler, false);
      
      // Media queries for responsive layouts
      const smQuery = ResponsiveUtils.getFrameworkQuery(ResponsiveUtils.FRAMEWORK_QUERY_KEY.SM_ONLY);
      this.smScreen = ResponsiveKnockoutUtils.createMediaQueryObservable(smQuery);
      const mdQuery = ResponsiveUtils.getFrameworkQuery(ResponsiveUtils.FRAMEWORK_QUERY_KEY.MD_UP);
      this.mdScreen = ResponsiveKnockoutUtils.createMediaQueryObservable(mdQuery);
      
      // Navigation data with page configurations
      let navData = [
        { path: '', redirect: 'dashboard' },
        { 
          path: 'dashboard', 
          detail: { 
            label: 'Card Management', 
            iconClass: 'oj-ux-ico-wallet',
            title: 'Card Management'
          } 
        },
        { 
          path: 'incidents', 
          detail: { 
            label: 'Cheque Management', 
            iconClass: 'oj-ux-ico-file',
            title: 'Cheque Management'
          } 
        },
        { 
          path: 'customers', 
          detail: { 
            label: 'Pay Order', 
            iconClass: 'oj-ux-ico-dollar',
            title: 'Pay Order '
          } 
        },
        { 
          path: 'about', 
          detail: { 
            label: 'Scheduled Transaction', 
            iconClass: 'oj-ux-ico-calendar',
            title: ' Scheduled Transaction'
          } 
        },
        { 
          path: 'investments', 
          detail: { 
            label: 'Al-Meezan Investment', 
            iconClass: 'oj-ux-ico-bar-chart',
            title: 'Al-Meezan Investment'
          } 
        },
        { 
          path: 'pfm', 
          detail: { 
            label: 'PFM', 
            iconClass: 'oj-ux-ico-analytics',
            title: 'PFM'
          } 
        },
        { 
          path: 'accounts', 
          detail: { 
            label: 'Accounts', 
            iconClass: 'oj-ux-ico-contact-group',
            title: 'Accounts'
          } 
        },
        { 
          path: 'myprofile', 
          detail: { 
            label: 'My Profile', 
            iconClass: 'oj-ux-ico-contact',
            title: 'My Profile'
          } 
        },
        { 
          path: 'setting', 
          detail: { 
            label: 'Setting', 
            iconClass: 'oj-ux-ico-menu',
            title: 'Setting'
          } 
        },
        { 
          path: 'notifications', 
          detail: { 
            label: 'Notifications', 
            iconClass: 'oj-ux-ico-message',
            title: 'Notifications'
          } 
        },
        { 
          path: 'help', 
          detail: { 
            label: 'Help', 
            iconClass: 'oj-ux-ico-list',
            title: 'Help'
          } 
        },
        { 
          path: 'editprofile', 
          detail: { 
            label: 'Edit Profile', 
            iconClass: 'oj-ux-ico-fire',
            title: 'Edit Profile'
          } 
        },
        { 
          path: 'otpscreen', 
          detail: { 
            label: 'OTP Screen', 
            iconClass: 'oj-ux-ico-fire',
            title: 'OTP Screen',
            view: 'js/views/otpscreen.html',       // Path to your HTML
            viewModel: 'js/viewModels/otpscreen'
          } 
        }
       
      ];
      
      // Router setup
      let router = new CoreRouter(navData, {
        urlAdapter: new UrlParamAdapter()
      });
      router.sync();
      
      this.moduleAdapter = new ModuleRouterAdapter(router);
      this.selection = new KnockoutRouterAdapter(router);
      
      // Setup the navDataProvider with the routes, excluding the first redirected route
// Exclude editprofile so it doesn’t appear in sidebar
      let navMenuItems = navData.filter(item => 
          item.path !== 'editprofile' && 
          item.path !== 'otpscreen' &&   // 🚀 this hides OTP screen
          item.path !== ''
        );
      this.navDataProvider = new ArrayDataProvider(navMenuItems, {keyAttributes: "path"});

      // Sidebar drawer state
      this.sideDrawerOn = ko.observable(false);
      
      // Current page title observable
      this.currentPageTitle = ko.observable('Dashboard');
      
      // Update page title when selection changes
      this.selection.path.subscribe((newPath) => {
        if (newPath) {
          const currentRoute = navData.find(route => route.path === newPath);
          if (currentRoute && currentRoute.detail) {
            this.currentPageTitle(currentRoute.detail.title || currentRoute.detail.label);
          }
        }
      });
      
      // Close drawer on medium and larger screens
      this.mdScreen.subscribe(() => { 
        if (this.mdScreen()) {
          this.sideDrawerOn(false);
        }
      });
      
      // Toggle drawer function
      this.toggleDrawer = () => {
        this.sideDrawerOn(!this.sideDrawerOn());
      };
      
      // Close drawer when navigation item is selected (for mobile)
      this.selection.path.subscribe(() => {
        if (!this.mdScreen()) {
          this.sideDrawerOn(false);
        }
      });
      
      // Initialize with default route if needed
      if (!router.currentState.path) {
        router.go({ path: 'dashboard' });
      }
     }
     
     // Release the application bootstrap busy state
     Context.getPageContext().getBusyContext().applicationBootstrapComplete();
     
     return new ControllerViewModel();
  }
);