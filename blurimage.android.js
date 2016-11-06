var React = require('React');
var { requireNativeComponent, View } = require('react-native');
const { PropTypes } = React

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
