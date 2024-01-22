export const data = {
    "appstle_subscription_widget": {
        "displayName": "Customise Widget Appearence",
        
        "subscriptionWidgetTextColor": {
            id: "subscriptionWidgetTextColor",
            displayName: 'Change text color of the Widget',
            placeholder: 'Click here to choose a color.',
            type: 'color',
            value: '',
            validation: '',
        },

        "subscriptionWidgetMarginTop": {
            id: "subscriptionWidgetMarginTop",
            displayName: 'Add space to the top of Widget',
            placeholder: 'Enter a number. Example - 10',
            type: 'input',
            value: '',
            validation: 'NUMBER',
        },

        "subscriptionWidgetMarginBottom": {
            id: "subscriptionWidgetMarginBottom",
            displayName: 'Add space to the bottom of Widget',
            placeholder: 'Enter a number. Example - 10',
            type: 'input',
            value: '',
            validation: 'NUMBER',
        },

        "subscriptionWrapperBorderWidth": {
            id: "subscriptionWrapperBorderWidth",
            displayName: 'Add Border thickness',
            placeholder: 'Enter a number. Example - 10',
            type: 'input',
            value: '',
            validation: 'NUMBER',
        },

        "subscriptionWrapperBorderColor": {
            id: "subscriptionWrapperBorderColor",
            displayName: 'Add Border Color',
            placeholder: 'Click here to choose a color.',
            type: 'color',
            value: '',
            validation: '',
        },

        "circleBorderColor": {
            id: "circleBorderColor",
            displayName: 'Adjust Color of Radio Button',
            placeholder: 'Click here to choose a color.',
            type: 'color',
            validation: '',
            value: '',
            validation: '',
            mappedFieldId: 'dotBackgroundColor'
        },

        "dotBackgroundColor": {
            id: "dotBackgroundColor",
            displayName: 'Change Color of Inner circle of Radio when Checked',
            placeholder: 'Click here to choose a color.',
            type: 'hidden',
            value: '',
            validation: '',
            },

        "tooltipSubscriptionSvgFill": {
            id: "tooltipSubscriptionSvgFill",
            displayName: 'Tooltip Icon Color',
            placeholder: 'Click here to choose a color.',
            type: 'color',
            validation: '',
            value: ''
        },

        "tooltipColor": {
            id: "tooltipColor",
            displayName: 'Tooltip Text Color',
            placeholder: 'Click here to choose a color.',
            type: 'color',
            validation: '',
            value: ''
        },
            
        "tooltipBackgroundColor": {
            id: "tooltipBackgroundColor",
            displayName: 'Tooltip Background Color',
            placeholder: 'Click here to choose a color.',
            type: 'color',
            value: '',
            mappedFieldId: 'tooltipBorderTopColorBorderTopColor'
        },

        "tooltipBorderTopColorBorderTopColor": {
            id: "tooltipBorderTopColorBorderTopColor",
            displayName: 'ToolTip Triangle Background Color',
            placeholder: 'Click here to choose a color.',
            type: 'hidden',
            validation: '',
            value: ''
        },

        "subscriptionFinalPriceColor": {
            id: "subscriptionFinalPriceColor",
            displayName: 'Subscription Final Price Text Color',
            placeholder: 'Click here to choose a color.',
            type: 'color',
            validation: '',
            value: ''
        },
        
    },

    "appstle_select": {
        "displayName": "Customise Subscription plan Select Dropdown",
        "selectPaddingTop": {
            id: "selectPaddingTop",
            displayName: 'Padding Top',
            placeholder: 'Enter a number. Example - 10',
            type: 'input',
            value: '',
            validation: 'NUMBER',
            },
        "selectPaddingBottom": {
            id: "selectPaddingBottom",
            displayName: 'Padding Bottom',
            placeholder: 'Enter a number. Example - 10',
            type: 'input',
            value: '',
            validation: 'NUMBER',
        },
        "selectPaddingLeft": {
            id: "selectPaddingLeft",
            displayName: 'Padding Left',
            placeholder: 'Enter a number. Example - 10',
            type: 'input',
            value: '',
            validation: 'NUMBER',
        },
        "selectPaddingRight": {
            id: "selectPaddingRight",
            displayName: 'Padding Right',
            placeholder: 'Enter a number. Example - 10',
            type: 'input',
            value: '',
            validation: 'NUMBER',
        },
        "selectBorderRadius": {
            id: "selectBorderRadius",
            displayName: 'Add Curves to Corners',
            placeholder: 'Enter a number. Example - 5',
            type: 'input',
            value: '',
            validation: 'NUMBER',
           
        },
        "selectBorderColor": {
            id: "selectBorderColor",
            displayName: 'Add Border Color to Widget',
            placeholder: 'Click here to choose a color.',
            type: 'color',
            validation: '',
            value: '',
            validation: '',
        },

        "selectBorderWidth": {
            id: "selectBorderWidth",
            displayName: 'Add Border thickness',
            placeholder: 'Enter a number. Example - 10',
            type: 'input',
            value: '',
            validation: 'NUMBER',
        },

        "selectBorderStyle": {
            id: "selectBorderStyle",
            displayName: 'Change Border Style (Solid)',
            placeholder: 'Select a style from dropdown. Example - Solid',
            type: 'dropdown',
            dropdownValues: ['none', 'solid', 'dotted', 'dashed', 'double', 'hidden', 'groove', 'ridge', 'inset', 'outset'],
            validation: ''
        },
    },
}
