export class Util {
  static calculateDimensions(xOffset, yOffset) {
    console.log('resizing?');
    const windowDimensions = [window.innerWidth, window.innerHeight];
    return {
      width: windowDimensions[0] - xOffset,
      height: windowDimensions[1] - yOffset
    };
  }
}

module.exports = Util;
