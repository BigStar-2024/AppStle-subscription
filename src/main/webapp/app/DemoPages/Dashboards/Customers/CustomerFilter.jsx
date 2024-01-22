import React, { useEffect, useState } from 'react'
import { Card, Button, Row, Col, CardBody, CardFooter, Input} from 'reactstrap';
import _ from "lodash";
import Switch from 'react-switch';

function CustomerFilter(props) {
    const { filterVal } = props;
    const [loaded, setLoaded] = useState(false)
    const [name, setName] = useState("")
    const [email, setEmail] = useState("")
    const [activeMoreThanOneSubscription, setActiveMoreThanOneSubscription] = useState(!!filterVal.activeMoreThanOneSubscription)
    console.log("filter",filterVal);
    console.log(activeMoreThanOneSubscription);
   
    const onApply = () => {
        if (props.onApply) {
            props.onApply({
                name: name || undefined,
                email: email || undefined,
                activeMoreThanOneSubscription: activeMoreThanOneSubscription
            })
        }
    }

    const onReset = () => {
        if (props.onApply) {
            props.onApply({})
        }
    }
    const onCancel = () => {
        if (props.onCancel) {
            props.onCancel();
        }
    }
    useEffect(() => {
        if (filterVal) {
            if (filterVal.name) {
                setName(filterVal.name);
            }
            if (filterVal.email) {
                setEmail(filterVal.email);
            }
        }
        setLoaded(true);
        return () => { }
    }, [])
    return (
        <Card>
            {loaded &&
                <CardBody>

                    <Row className="mt-3" >
                        <Col sm="4">
                            <label className="font-weight-bold">Name</label>
                        </Col>
                        <Col sm="8" >
                            <Input type="text" name="name" defaultValue={name} onChange={(e) => setName(e.target.value)} >
                            </Input>
                        </Col>
                    </Row>
                    <Row className="mt-3">
                        <Col sm="4">
                            <label className="font-weight-bold">Email</label>
                        </Col>
                        <Col sm="8" >
                            <Input type="text" name="email" defaultValue={email} onChange={(e) => setEmail(e.target.value)} >
                            </Input>
                        </Col>
                    </Row>

                    <Row className="mt-3">
                        <Col sm="4">
                            <label className="font-weight-bold">Having more than 1 active subscription</label>
                        </Col>
                        <Col sm="8" >
                            <Switch
                                checked={activeMoreThanOneSubscription}
                                onChange={
                                    (e) => setActiveMoreThanOneSubscription(e)
                                }
                                onColor="#86d3ff"
                                onHandleColor="#2693e6"
                                handleDiameter={20}
                                // uncheckedIcon={activeMoreThanOneSubscription}
                                checkedIcon={activeMoreThanOneSubscription}
                                boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                activeBoxShadow="0px 0px 1px 10px rgba(0, 0, 0, 0.2)"
                                height={17}
                                width={36}
                                className="ml-2 mb-2"
                                id="material-switch"
                            />
                        </Col>
                    </Row>

                </CardBody>

            }
            <CardFooter>
                <div >
                    <Button color="default" onClick={onReset}>Reset</Button>
                </div>
                <div className="ml-auto">
                    <Button color="default" onClick={onCancel}>Cancel</Button>
                    <Button color="primary" onClick={onApply} >Apply</Button>
                </div>
            </CardFooter>
        </Card>

    )

}

export default CustomerFilter
