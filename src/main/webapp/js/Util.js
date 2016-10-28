export class Util {
  static calculateDimensions(xOffset, yOffset) {
    const windowDimensions = [window.innerWidth, window.innerHeight];
    return {
      width: windowDimensions[0] - xOffset,
      height: windowDimensions[1] - yOffset
    };
  }

  static fadeElement(id, callback) {
    let element = document.getElementById(id);
    element.classList.add('fade-element');
    setTimeout(() => {
      if (callback != null) {
        callback();
      }
      element.classList.remove('fade-element');
    }, 500);
  }
}

module.exports = Util;
