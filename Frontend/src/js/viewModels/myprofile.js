define(["knockout", "../accUtils"], function (ko, accUtils) {
  function MyProfileViewModel(params) {
    var self = this;

    // 🟢 Use CNIC as primary ID (matches backend)
    self.cnic = ko.observable(3265874859999);

    // User fields
    self.name = ko.observable();
    self.dateOfBirth = ko.observable();
    self.registeredHomeAddress = ko.observable();
    self.registeredContactNumber = ko.observable();
    self.registeredEmailAddress = ko.observable();
    self.city = ko.observable();
    self.country = ko.observable();
    self.cnicExpirationDate = ko.observable();
    self.lastLoginDetails = ko.observable();

    // Account fields
    self.accountTitle = ko.observable();
    self.fileStatus = ko.observable();
    self.zakatDeductionStatus = ko.observable();

    // ✅ Computed: show "City, Country"
    self.formattedLocation = ko.computed(() => {
      const city = self.city() || "";
      const country = self.country() || "";
      if (city && country) return `${city}, ${country}`;
      else return city || country || "N/A";
    });

    /** ✅ Load profile from backend */
    self.loadProfile = async function () {
      const apiUrl = `http://localhost:8080/api/bycnic/${self.cnic()}?t=${new Date().getTime()}`;
      console.log("Fetching profile for CNIC:", self.cnic());

      try {
        const response = await fetch(apiUrl);
        if (!response.ok) throw new Error("Failed to fetch profile");

        const profile = await response.json();
        console.log("profile", profile);

        // ✅ Populate user details
        self.name(profile.name);
        self.dateOfBirth(profile.dateOfBirth);
        self.registeredHomeAddress(profile.registeredHomeAddress);
        self.registeredContactNumber(profile.registeredContactNumber);
        self.registeredEmailAddress(profile.registeredEmailAddress);
        self.city(profile.city);
        self.country(profile.country);
        self.cnicExpirationDate(profile.cnicExpirationDate);
        self.lastLoginDetails(profile.lastLoginDetails);

        // ✅ Populate account (take first if multiple)
        if (profile.accounts && profile.accounts.length > 0) {
          const account = profile.accounts[1];
          self.accountTitle(account.accountTitle);
          self.fileStatus(account.fileStatus);
          self.zakatDeductionStatus(account.zakatDeductionStatus);
        }

        // ✅ Store for later use
        localStorage.setItem("currentUser", JSON.stringify(profile));
      } catch (error) {
        console.error("Error fetching profile data:", error);
      }
    };

    // Auto reload when CNIC changes
    self.cnic.subscribe(() => {
      self.loadProfile();
    });

    const { router } = params;
    self.move = () => {
      localStorage.setItem("editSource", "myprofile");
      // 🟢 Ensure the latest profile data is saved before navigatio
      router.go({ path: "editprofile" });
    };

    this.connected = () => {
      accUtils.announce("My Profile page loaded.", "assertive");
      document.title = "My Profile";

      // ✅ Small UI success toast handler
      setTimeout(() => {
        if (localStorage.getItem("showProfileUpdateSuccess") === "true") {
          const successBar = document.getElementById("update-success");
          if (successBar) {
            successBar.classList.remove("hidden");

            setTimeout(() => {
              successBar.classList.add("fade-out");
              setTimeout(() => {
                successBar.classList.add("hidden");
                successBar.classList.remove("fade-out");
              }, 500);
            }, 2000);

            localStorage.removeItem("showProfileUpdateSuccess");
          } else {
            console.warn("⚠️ update-success element not found in DOM.");
          }
        }
      }, 300);
    };

    // ✅ Initial load
    self.loadProfile();
  }

  return MyProfileViewModel;
});
