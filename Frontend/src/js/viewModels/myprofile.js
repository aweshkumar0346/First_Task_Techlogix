define(['knockout', '../accUtils'], 
function(ko, accUtils) {
  function MyProfileViewModel(params) {
    var self = this;

    self.id = ko.observable(5); // 🟢 hardcoded user ID (for now)
    self.name = ko.observable();
    self.accountTitle = ko.observable();
    self.jobStatus = ko.observable();
    self.dateCreationStatus = ko.observable();
    self.ercEmployeeStatus = ko.observable();
    self.lastLoginDetails = ko.observable();
    self.dateOfBirth = ko.observable();
    self.registeredHomeAddress = ko.observable();
    self.registeredContactNumber = ko.observable();
    self.registeredEmailAddress = ko.observable();
    //self.location = ko.observable();
    self.city = ko.observable();
    self.country = ko.observable();

    // ✅ Computed observable to show “City, Country”
    self.formattedLocation = ko.computed(() => {
      const city = self.city() || '';
      const country = self.country() || '';
      if (city && country) return `${city}, ${country}`;
      else return city || country || 'N/A';
    });

    self.loadProfile = async function () {
      // Add timestamp to prevent browser caching
      const apiUrl = `http://localhost:8080/byid/${self.id()}?t=${new Date().getTime()}`;

      //const apiUrl = `http://localhost:8080/byid/${self.id()}`;
      console.log("Fetching profile for ID:", self.id());
      try {
        const response = await fetch(apiUrl);
        if (!response.ok) throw new Error("Failed to fetch profile");

        const profile = await response.json();

        // ✅ Populate observables
        self.name(profile.name);
        self.accountTitle(profile.accountTitle);
        self.jobStatus(profile.jobStatus);
        self.dateCreationStatus(profile.dateCreationStatus);
        self.ercEmployeeStatus(profile.ercEmployeeStatus);
        self.lastLoginDetails(profile.lastLoginDetails);
        self.dateOfBirth(profile.dateOfBirth);
        self.registeredHomeAddress(profile.registeredHomeAddress);
        self.registeredContactNumber(profile.registeredContactNumber);
        self.registeredEmailAddress(profile.registeredEmailAddress);
        //self.location(profile.location);
        self.city(profile.city);
        self.country(profile.country);

        // ✅ Store cleanly for edit profile page
        localStorage.removeItem("currentUser");
        localStorage.setItem("currentUser", JSON.stringify(profile));

      } catch (error) {
        console.error("Error fetching profile data:", error);
      }

      
    };

    // Subscribe to ID changes to automatically reload profile
    self.id.subscribe(() => {
      self.loadProfile();
    });
    
    const { router } = params;
    self.move = () => {
      router.go({ path: "editprofile" });
    };

    this.connected = () => {
      accUtils.announce('My Profile page loaded.', 'assertive');
      document.title = "My Profile";
      self.loadProfile();
    };
  }

  return MyProfileViewModel;
});
