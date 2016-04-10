var { requireNativeComponent, PropTypes, View } = require('react-native');

var iface = {
    name: 'BlurImageView',
    propTypes: {
        ...View.propTypes,
        radius: PropTypes.number,
        sampling: PropTypes.number,
        imageUrl: PropTypes.string,
    },
};

module.exports = requireNativeComponent('RCTBlurImageView', iface);