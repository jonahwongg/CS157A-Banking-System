document.addEventListener("DOMContentLoaded", () => {
    const flash = document.querySelector(".flash");
    if (flash) {
        setTimeout(() => {
            flash.style.opacity = "0";
            flash.style.transform = "translateY(-8px)";
        }, 3500);
    }

    const tabButtons = Array.from(document.querySelectorAll(".tab-button"));
    const tabPanels = Array.from(document.querySelectorAll(".tab-panel"));
    const pageShell = document.querySelector(".page-shell");

    if (tabButtons.length > 0 && tabPanels.length > 0) {
        const activateTab = (targetId) => {
            tabButtons.forEach((button) => {
                button.classList.toggle("is-active", button.dataset.tabTarget === targetId);
            });
            tabPanels.forEach((panel) => {
                panel.classList.toggle("is-active", panel.id === targetId);
            });
            document.querySelectorAll('input[name="activeTab"]').forEach((input) => {
                input.value = targetId;
            });
        };

        tabButtons.forEach((button) => {
            button.addEventListener("click", () => activateTab(button.dataset.tabTarget));
        });

        activateTab(pageShell?.dataset.activeTab || "customers-tab");
    }
});
