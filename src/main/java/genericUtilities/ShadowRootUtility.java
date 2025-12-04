package genericUtilities;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ShadowRootUtility {
	/**
	 * This method retrieves a WebElement located inside one or more nested
	 * Shadow DOMs using a CSS selector. Since standard Selenium locators
	 * cannot directly access shadow-root elements, this method executes
	 * a custom JavaScript function that traverses all shadow roots in the
	 * document until the target element is found.
	 *
	 * @param driver The WebDriver instance used to execute the JavaScript.
	 * @param css The CSS selector of the target element inside Shadow DOM.
	 * @return The WebElement found inside nested Shadow DOMs.
	 * @throws RuntimeException If the element cannot be located.
	 */
	public static WebElement getShadowElement(WebDriver driver, String css) {

	    JavascriptExecutor js = (JavascriptExecutor) driver;

	    String deepFind =
	            "function deepQuery(selector) {" +
	            "   function search(node) {" +
	            "       let found = null;" +
	            "       try { found = node.querySelector(selector); } catch(e) {}" +
	            "       if (found) return found;" +
	            "       let all = node.querySelectorAll('*');" +
	            "       for (let el of all) {" +
	            "           if (el.shadowRoot) {" +
	            "               let inner = search(el.shadowRoot);" +
	            "               if (inner) return inner;" +
	            "           }" +
	            "       }" +
	            "       return null;" +
	            "   }" +
	            "   return search(document);" +
	            "}" +
	            "return deepQuery(arguments[0]);";

	    Object obj = js.executeScript(deepFind, css);

	    if (!(obj instanceof WebElement)) {
	        throw new RuntimeException("Element NOT FOUND inside shadow DOM: " + css);
	    }

	    return (WebElement) obj;
	}
	
	/**
	 * Forces all shadow roots in a Chromium-based browser to be open (mode: 'open')
	 * so that WebDriver can access elements inside closed shadow DOMs.
	 *
	 * <p>
	 * This method patches the native {@code attachShadow} function by overriding it
	 * to always use {@code mode: 'open'}. It only works for Chromium-based browsers
	 * (Chrome, Edge, etc.) and must be executed before any shadow DOMs are created.
	 * </p>
	 *
	 * @param driver The WebDriver instance (must be ChromiumDriver for this to work).
	 *               If another browser is used, the method logs a warning and does nothing.
	 */
	public void forceAllShadowRootsOpen(WebDriver driver) {

	    // Applicable only in Chromium-based browsers
	    if (driver instanceof org.openqa.selenium.chromium.ChromiumDriver) {

	        String patchJS =
	                "(function() {" +
	                "  const original = Element.prototype.attachShadow;" +
	                "  Element.prototype.attachShadow = function(init) {" +
	                "    init = Object.assign({}, init, { mode: 'open' });" +
	                "    const shadow = original.call(this, init);" +
	                "    Object.defineProperty(this, 'shadowRoot', { value: shadow });" +
	                "    return shadow;" +
	                "  };" +
	                "})();";

	        Map<String, Object> params = new HashMap<>();
	        params.put("source", patchJS);

	        ((org.openqa.selenium.chromium.ChromiumDriver) driver)
	                .executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", params);
	        return;
	    }
	    System.out.println("forceAllShadowRootsOpen is NOT supported in this browser. "
	            + "Closed shadow DOM cannot be opened by WebDriver.");
	}


}
