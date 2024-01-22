import React from 'react';
import * as Tabs from '@radix-ui/react-tabs';
import './styles.scss';
import CustomDateSelector from 'app/DemoPages/Dashboards/Analytics/CustomDate';
import Overview from 'app/DemoPages/Dashboards/Analytics/Tabs/Overview';
import Subscriptions from 'app/DemoPages/Dashboards/Analytics/Tabs/Subscriptions';
import Revenue from 'app/DemoPages/Dashboards/Analytics/Tabs/Revenue';

export default function AnalyticsTabs() {
  return (
    <Tabs.Root className="TabsRoot" defaultValue="tab1">
      <Tabs.List className="TabsList" aria-label="Manage your account">
        <Tabs.Trigger className="TabsTrigger" value="tab1">
          Overview
        </Tabs.Trigger>
        <Tabs.Trigger className="TabsTrigger" value="tab2">
          Active Subscribers
        </Tabs.Trigger>
        <Tabs.Trigger className="TabsTrigger" value="tab3">
          Revenue
        </Tabs.Trigger>
      </Tabs.List>
      <Tabs.Content className="TabsContent" value="tab1">
        <CustomDateSelector />
        <Overview />
      </Tabs.Content>
      <Tabs.Content className="TabsContent" value="tab2">
        <CustomDateSelector />
        <Subscriptions />
      </Tabs.Content>
      <Tabs.Content className="TabsContent" value="tab3">
        <CustomDateSelector />
        <Revenue />
      </Tabs.Content>
    </Tabs.Root>
  );
}
