/**
 * @license
 * Copyright (c) 2014, 2025, Oracle
 * Licensed under The Universal Permissive License (UPL), Version 1.0
 * @ignore
 */
define(["knockout", "../accUtils"], function (ko, accUtils) {
  function OTPScreenViewModel(params) {
    var self = this;
    const { router } = params;
    console.log("🟢 OTPScreenViewModel loaded");

    // 🔹 Navigation
   // 🔹 Navigation
self.goBack = () => {
  localStorage.setItem("editSource", "otpscreen");
  router.go({ path: "editprofile" });
};

self.goNext = () => {
  localStorage.removeItem("editSource");
  router.go({ path: "myprofile" });
};


    // 🔹 OTP input observables (6 digits)
    self.otpDigits = ko.observableArray([
      ko.observable(""),
      ko.observable(""),
      ko.observable(""),
      ko.observable(""),
      ko.observable(""),
      ko.observable(""),
    ]);

    // 🔹 Timer (5 minutes)
    self.timeLeft = ko.observable(120); // 5 min = 300 sec
    self.formattedTime = ko.computed(() => {
      var minutes = Math.floor(self.timeLeft() / 60);
      var seconds = self.timeLeft() % 60;
      return minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
    });

    // ⏳ Countdown timer logic
    var interval = setInterval(() => {
      if (self.timeLeft() > 0) self.timeLeft(self.timeLeft() - 1);
      else clearInterval(interval);
    }, 1000);

    // 🔹 Auto-jump and digit-only input (fixed for live update)
    self.handleKeyUp = function (data, event) {
      const input = event.target;
      const index = parseInt(input.getAttribute("data-index"));
      const value = input.value.replace(/[^0-9]/g, ""); // only digits allowed

      // Update observable directly (fix: live update)
      self.otpDigits()[index](value);

      // Move forward when a digit is entered
      if (value.length === 1 && event.key !== "Backspace") {
        const next = input.nextElementSibling;
        if (next && next.tagName === "INPUT") next.focus();
      }

      // Move back on backspace if empty
      if (event.key === "Backspace" && value === "") {
        const prev = input.previousElementSibling;
        if (prev && prev.tagName === "INPUT") prev.focus();
      }

      return true;
    };

    // 🔹 Verify OTP and update backend
    self.verifyOtp = async function () {
      console.log("✅ verifyOtp() called");
      const email = localStorage.getItem("otpEmail");
      const otp = self
        .otpDigits()
        .map((d) => (d() || "").trim())
        .join("");

      console.log(
        "OTP array raw:",
        self.otpDigits().map((d) => d())
      );
      console.log("Joined OTP:", otp);
      console.log("Email from localStorage:", email);

      if (!/^\d{6}$/.test(otp)) {
        alert("Please enter a valid 6-digit OTP.");
        return;
      }

      try {
        const verifyResponse = await fetch(
          "http://localhost:8080/api/verify-otp",
          {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email: email, otp: otp }),
          }
        );

        console.log("Fetch response status:", verifyResponse.status);

        if (!verifyResponse.ok) {
          const errMsg = await verifyResponse.text();
          throw new Error(errMsg || "OTP verification failed.");
        }

        const message = await verifyResponse.text();
        console.log("Server says:", message);
        alert("✅ OTP verified successfully!");

        const pendingUpdate = JSON.parse(localStorage.getItem("pendingUpdate"));
        console.log("Pending update data:", pendingUpdate);

        if (!pendingUpdate || !pendingUpdate.cnic) {
          alert("No profile data found to update.");
          return;
        }

        const updateResponse = await fetch(
          `http://localhost:8080/api/update/${pendingUpdate.cnic}`,
          {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(pendingUpdate),
          }
        );

        console.log("Update response status:", updateResponse.status);

        if (!updateResponse.ok) {
          const updateError = await updateResponse.text();
          throw new Error(updateError || "Profile update failed.");
        }

        const updatedUser = await updateResponse.json();
        alert("🎉 Profile updated successfully!");

        localStorage.setItem("showProfileUpdateSuccess", "true");
        localStorage.removeItem("otpEmail");
        localStorage.removeItem("pendingUpdate");

        router.go({ path: "myprofile" });
      } catch (err) {
        console.error("❌ OTP verification failed or error:", err);
        alert("OTP verification failed or was cancelled.");

        // 🧹 Clean up temporary data — don’t apply edits
        localStorage.removeItem("otpEmail");
        localStorage.removeItem("pendingUpdate");

        // 🔙 Go back to MyProfile (original data)
        router.go({ path: "myprofile" });
      }
    };

    // 🔹 Lifecycle
    this.connected = () => {
      accUtils.announce("OTP Screen loaded.", "assertive");
      document.title = "OTP Verification";
    };
    this.disconnected = () => {};
    this.transitionCompleted = () => {};
  }

  return OTPScreenViewModel;
});
